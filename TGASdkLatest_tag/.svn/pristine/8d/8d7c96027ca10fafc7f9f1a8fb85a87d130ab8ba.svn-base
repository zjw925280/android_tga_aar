/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sg.just4fun.tgasdk.web.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains helper static methods for dealing with the Payments API.
 *
 * <p>Many of the parameters used in the code are optional and are set here merely to call out their
 * existence. Please consult the documentation to learn more and feel free to remove ones not
 * relevant to your implementation.
 */

@SuppressWarnings("ALL")
public class GoogleBillingUtil {

  private static final String TAG = "```GoogleBillingUtil";
  private static final boolean IS_DEBUG = true;
  private static String[] inAppSKUS;
  private String[] subsSKUS = new String[]{"6"};//订阅ID,必填

  public static final String BILLING_TYPE_INAPP = BillingClient.SkuType.INAPP;//内购
  public static final String BILLING_TYPE_SUBS = BillingClient.SkuType.SUBS;//订阅

  private static BillingClient mBillingClient;
  private static BillingClient.Builder builder ;
  private static OnPurchaseFinishedListener mOnPurchaseFinishedListener;
  private static OnStartSetupFinishedListener mOnStartSetupFinishedListener ;
  private static OnQueryFinishedListener mOnQueryFinishedListener;

  private boolean isAutoConsumeAsync = true;//是否在购买成功后自动消耗商品

  private static final GoogleBillingUtil mGoogleBillingUtil = new GoogleBillingUtil() ;

  private GoogleBillingUtil()
  {

  }

  public static GoogleBillingUtil getInstance(String productId)
  {
    //googleplay应用内商品栏，添加商品后得到
  inAppSKUS = new String[]{productId};//内购ID,必填
    cleanListener();
    return mGoogleBillingUtil;
  }

  public GoogleBillingUtil build(Context context)
  {
    if(mBillingClient==null)
    {
      synchronized (mGoogleBillingUtil)
      {
        if(mBillingClient==null)
        {
          if(isGooglePlayServicesAvailable(context))
          {
            builder = BillingClient.newBuilder(context);
            mBillingClient = builder.setListener(mGoogleBillingUtil.new MyPurchasesUpdatedListener()).enablePendingPurchases().build();
          }
          else
          {
            if(IS_DEBUG)
            {
              log("警告:GooglePlay服务处于不可用状态，请检查");
            }
            if(mOnStartSetupFinishedListener!=null)
            {
              mOnStartSetupFinishedListener.onSetupError();
            }
          }
        }
        else
        {
          builder.setListener(mGoogleBillingUtil.new MyPurchasesUpdatedListener());
        }
      }
    }
    else
    {
      builder.setListener(mGoogleBillingUtil.new MyPurchasesUpdatedListener());
    }
    synchronized (mGoogleBillingUtil)
    {
      if(mGoogleBillingUtil.startConnection())
      {
        mGoogleBillingUtil.queryInventoryInApp();
//        mGoogleBillingUtil.queryInventorySubs();
        mGoogleBillingUtil.queryPurchasesInApp();
      }
    }
    return mGoogleBillingUtil;
  }

  public boolean startConnection()
  {
    if(mBillingClient==null)
    {
      log("初始化失败:mBillingClient==null");
      return false;
    }
    if(!mBillingClient.isReady())
    {
      mBillingClient.startConnection(new BillingClientStateListener() {

        @Override
        public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
          if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            queryInventoryInApp();
//            queryInventorySubs();
            queryPurchasesInApp();
            if(mOnStartSetupFinishedListener!=null)
            {
              mOnStartSetupFinishedListener.onSetupSuccess();
            }
          }
          else
          {
            log("初始化失败:onSetupFail:code="+billingResult.getResponseCode());
            if(mOnStartSetupFinishedListener!=null)
            {
              mOnStartSetupFinishedListener.onSetupFail(billingResult.getResponseCode());
            }
          }
        }

        @Override
        public void onBillingServiceDisconnected() {
          if(mOnStartSetupFinishedListener!=null)
          {
            mOnStartSetupFinishedListener.onSetupError();
          }
          log("初始化失败:onBillingServiceDisconnected");
        }
      });
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * Google购买商品回调接口(订阅和内购都走这个接口)
   */
  private class MyPurchasesUpdatedListener implements PurchasesUpdatedListener
  {

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
      if(mOnPurchaseFinishedListener==null)
      {
        if(IS_DEBUG)
        {
          log("警告:接收到购买回调，但购买商品接口为Null，请设置购买接口。eg:setOnPurchaseFinishedListener()");
        }
        return ;
      }
      if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK&&list!=null)
      {
        if(isAutoConsumeAsync)
        {
          //消耗商品
          for(Purchase purchase:list)
          {
            String sku = purchase.getSku();
            if(sku!=null)
            {
              String skuType = getSkuType(sku);
              if(skuType!=null)
              {
                if(skuType.equals(BillingClient.SkuType.INAPP))
                {
                  consumeAsync(purchase.getPurchaseToken());
                }
              }
            }
          }
        }
        mOnPurchaseFinishedListener.onPurchaseSuccess(list);
      }
      else
      {
        mOnPurchaseFinishedListener.onPurchaseFail(billingResult.getResponseCode());
      }
    }
  }

  /**
   * 查询内购商品信息
   */
  public void queryInventoryInApp()
  {
    queryInventory(BillingClient.SkuType.INAPP);
  }

  /**
   * 查询订阅商品信息
   */
  public void queryInventorySubs()
  {
    queryInventory(BillingClient.SkuType.SUBS);
  }

  private void queryInventory(final String skuType) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if (mBillingClient == null)
        {
          if(mOnQueryFinishedListener!=null)
          {
            mOnQueryFinishedListener.onQueryError();
          }
          return ;
        }
        ArrayList<String> skuList = new ArrayList<>();
        if(skuType.equals(BillingClient.SkuType.INAPP))
        {
          Collections.addAll(skuList, inAppSKUS);
        }
        else if(skuType.equals(BillingClient.SkuType.SUBS))
        {
          Collections.addAll(skuList, subsSKUS);
        }
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(skuType);
        mBillingClient.querySkuDetailsAsync(params.build(),new MySkuDetailsResponseListener(mOnQueryFinishedListener,skuType));
      }
    };
    executeServiceRequest(runnable);
  }

  /**
   * Google查询商品信息回调接口
   */
  private class MySkuDetailsResponseListener implements SkuDetailsResponseListener
  {
    private OnQueryFinishedListener mOnQueryFinishedListener ;
    private String skuType ;
    private List<SkuDetails> list=new ArrayList<>();

    public MySkuDetailsResponseListener(OnQueryFinishedListener onQueryFinishedListener,String skuType) {
      mOnQueryFinishedListener = onQueryFinishedListener;
      this.skuType = skuType;
    }


    @Override
    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
      this.list=list;
      if(mOnQueryFinishedListener==null)
      {
        if(IS_DEBUG) {
          log("警告:接收到查询商品回调，但查询商品接口为Null，请设置购买接口。eg:setOnQueryFinishedListener()");
        }
        return ;
      }
      if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK&&list!=null)
      {
        mOnQueryFinishedListener.onQuerySuccess(skuType,list);
      }
      else
      {
        mOnQueryFinishedListener.onQueryFail(billingResult.getResponseCode());
      }
    }
  }

  /**
   * 发起内购
   * @param skuId
   * @return
   */
  public void purchaseInApp(Activity activity,SkuDetails skuId)
  {
    purchase(activity,skuId, BillingClient.SkuType.INAPP);
  }

  /**
   * 发起订阅
   * @param skuId
   * @return
   */
  public void purchaseSubs(Activity activity,SkuDetails skuId)
  {
    purchase(activity,skuId, BillingClient.SkuType.SUBS);
  }

  private void purchase(Activity activity,final SkuDetails skuId,final String skuType)
  {
    if(mBillingClient==null)
    {
      if(mOnPurchaseFinishedListener!=null)
      {
        mOnPurchaseFinishedListener.onPurchaseError();
      }
      return ;
    }
    if(startConnection())
    {

      BillingFlowParams flowParams = BillingFlowParams.newBuilder()
              .setSkuDetails(skuId)
              .build();
      mBillingClient.launchBillingFlow(activity,flowParams);
    }
    else
    {
      if(mOnPurchaseFinishedListener!=null)
      {
        mOnPurchaseFinishedListener.onPurchaseError();
      }
    }
  }

  /**
   * 消耗商品
   * @param purchaseToken
   */
  public void consumeAsync(String purchaseToken)
  {
    if(mBillingClient==null)
    {
      return ;
    }

    ConsumeParams consumeParams =
            ConsumeParams.newBuilder()
                    .setPurchaseToken(purchaseToken)
                    .build();
    mBillingClient.consumeAsync(consumeParams, new MyConsumeResponseListener());
  }

  /**
   * Googlg消耗商品回调
   */
  private class MyConsumeResponseListener implements ConsumeResponseListener
  {

    @Override
    public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
      if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
          Log.e("googlePayWay","消耗成功");
      }
    }
  }


  /**
   * 获取已经内购的商品
   * @return
   */
  public List<Purchase> queryPurchasesInApp()
  {
    return queryPurchases(BillingClient.SkuType.INAPP);
  }

  /**
   * 获取已经订阅的商品
   * @return
   */
  public List<Purchase> queryPurchasesSubs()
  {
    return queryPurchases(BillingClient.SkuType.SUBS);
  }

  private List<Purchase> queryPurchases(String skuType)
  {
    if(mBillingClient==null)
    {
      return null;
    }
    if(!mBillingClient.isReady())
    {
      startConnection();
    }
    else
    {
      Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(skuType);
      if(purchasesResult!=null)
      {
        if(purchasesResult.getResponseCode()== BillingClient.BillingResponseCode.OK)
        {
          List<Purchase> purchaseList =  purchasesResult.getPurchasesList();
          if(isAutoConsumeAsync)
          {
            if(purchaseList!=null)
            {
              for(Purchase purchase:purchaseList)
              {
                if(skuType.equals(BillingClient.SkuType.INAPP))
                {
                  consumeAsync(purchase.getPurchaseToken());
                }
              }
            }
          }
          return purchaseList;
        }
      }

    }
    return null;
  }

  /**
   * 获取有效订阅的数量
   * @return -1查询失败，0没有有效订阅，>0具有有效的订阅
   */
  public int getPurchasesSizeSubs()
  {
    List<Purchase> list = queryPurchasesSubs();
    if(list!=null)
    {
      return list.size();
    }
    return -1;
  }

  /**
   * 通过sku获取订阅商品序号
   * @param sku
   * @return
   */
  public int getSubsPositionBySku(String sku)
  {
    return getPositionBySku(sku, BillingClient.SkuType.SUBS);
  }

  /**
   * 通过sku获取内购商品序号
   * @param sku
   * @return 成功返回需要 失败返回-1
   */
  public int getInAppPositionBySku(String sku)
  {
    return getPositionBySku(sku, BillingClient.SkuType.INAPP);
  }

  private int getPositionBySku(String sku,String skuType)
  {

    if(skuType.equals(BillingClient.SkuType.INAPP))
    {
      int i = 0;
      for(String s:inAppSKUS)
      {
        if(s.equals(sku))
        {
          return i;
        }
        i++;
      }
    }
    else if(skuType.equals(BillingClient.SkuType.SUBS))
    {
      int i = 0;
      for(String s:subsSKUS)
      {
        if(s.equals(sku))
        {
          return i;
        }
        i++;
      }
    }
    return -1;
  }

  private void executeServiceRequest(final Runnable runnable)
  {
    if(startConnection())
    {
      runnable.run();
    }
  }

  /**
   * 通过序号获取订阅sku
   * @param position
   * @return
   */
  public String getSubsSkuByPosition(int position)
  {
    if(position>=0&&position<subsSKUS.length)
    {
      return subsSKUS[position];
    }
    else {
      return null;
    }
  }

  /**
   * 通过序号获取内购sku
   * @param position
   * @return
   */
  public String getInAppSkuByPosition(int position)
  {
    if(position>=0&&position<inAppSKUS.length)
    {
      return inAppSKUS[position];
    }
    else
    {
      return null;
    }
  }

  /**
   * 通过sku获取商品类型(订阅获取内购)
   * @param sku
   * @return inapp内购，subs订阅
   */
  private String getSkuType(String sku)
  {
    if(Arrays.asList(inAppSKUS).contains(sku))
    {
      return BillingClient.SkuType.INAPP;
    }
    else if(Arrays.asList(subsSKUS).contains(sku))
    {
      return BillingClient.SkuType.SUBS;
    }
    return null;
  }

  /**
   * 检测GooglePlay服务是否可用(需要导入包api "com.google.android.gms:play-services-location:11.8.0"，也可以不检查，跳过这个代码)
   * @param context
   * @return
   */
  public static boolean isGooglePlayServicesAvailable(Context context)
  {
    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
    if(googleApiAvailability!=null)
    {
      int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
      return resultCode == ConnectionResult.SUCCESS;
    }
    return false;
//    return true;//不检查直接跳过
  }

  public GoogleBillingUtil setOnQueryFinishedListener(OnQueryFinishedListener onQueryFinishedListener) {
    mOnQueryFinishedListener = onQueryFinishedListener;
    return mGoogleBillingUtil;
  }

  public GoogleBillingUtil setOnPurchaseFinishedListener(OnPurchaseFinishedListener onPurchaseFinishedListener) {
    mOnPurchaseFinishedListener = onPurchaseFinishedListener;
    return mGoogleBillingUtil;
  }

  public OnStartSetupFinishedListener getOnStartSetupFinishedListener() {
    return mOnStartSetupFinishedListener;
  }

  public GoogleBillingUtil setOnStartSetupFinishedListener(OnStartSetupFinishedListener onStartSetupFinishedListener) {
    mOnStartSetupFinishedListener = onStartSetupFinishedListener;
    return mGoogleBillingUtil;
  }

  /**
   *  本工具查询回调接口
   */
  public interface OnQueryFinishedListener{
    //Inapp和sub都走这个接口查询的时候一定要判断skuType
    public void onQuerySuccess(String skuType, List<SkuDetails> list);
    public void onQueryFail(int responseCode);
    public void onQueryError();
  }

  /**
   * 本工具购买回调接口(内购与订阅都走这接口)
   */
  public interface OnPurchaseFinishedListener{

    public void onPurchaseSuccess(List<Purchase> list);

    public void onPurchaseFail(int responseCode);

    public void onPurchaseError();

  }

  /**
   * google服务启动接口
   */
  public interface OnStartSetupFinishedListener{
    public void onSetupSuccess();

    public void onSetupFail(int responseCode);

    public void onSetupError();
  }

  public boolean isReady() {
    return mBillingClient!=null&&mBillingClient.isReady();
  }

  public boolean isAutoConsumeAsync()
  {
    return isAutoConsumeAsync;
  }

  public void setIsAutoConsumeAsync(boolean isAutoConsumeAsync)
  {
    this.isAutoConsumeAsync= isAutoConsumeAsync;
  }

  /**
   * 清除所有监听器，防止内存泄漏
   * 如果有多个页面使用了支付，需要确保上个页面的cleanListener在下一个页面的GoogleBillingUtil.getInstance()前使用。
   * 所以不建议放在onDestory里调用
   */
  public static void cleanListener()
  {
    mOnPurchaseFinishedListener = null;
    mOnQueryFinishedListener = null;
    mOnStartSetupFinishedListener = null;
    if(builder!=null)
    {
      builder.setListener(null);
    }
  }

  /**
   * 断开连接google服务
   * 注意！！！一般情况不建议调用该方法，让google保留连接是最好的选择。
   */
  public static void endConnection()
  {
    //注意！！！一般情况不建议调用该方法，让google保留连接是最好的选择。
    if(mBillingClient!=null)
    {
      if(mBillingClient.isReady())
      {
        mBillingClient.endConnection();
        mBillingClient = null;
      }
    }
  }

  private static void log(String msg)
  {
    if(IS_DEBUG)
    {
      Log.e(TAG,msg);
    }
  }
  /**
   * 是否支持内购
   */
  public  boolean isIabServiceAvailable(Context context)
  {
    final PackageManager packageManager = context.getPackageManager();
    List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
    return list != null && list.size() > 0;
  }
  private Intent getBindServiceIntent()
  {
    Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
    intent.setPackage("com.android.vending");
    return intent;
  }
}
