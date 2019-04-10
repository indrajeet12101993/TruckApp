package com.truckintransit.operator.newtworking

import com.truckintransit.operator.pojo.ResponseFromServerAddVehicle
import com.truckintransit.operator.pojo.ResponseFromServerForDriverBasicDetails
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.adminDash.ResponseFromServerAdminDashBoard
import com.truckintransit.operator.pojo.bookingAccept.ResponseBookingAccept
import com.truckintransit.operator.pojo.catogoryDilaog.ResponseForCatogoryDialog
import com.truckintransit.operator.pojo.citylist.ResponseFromServerCityList
import com.truckintransit.operator.pojo.driverDash.ResponseFromServerDriverDshBoard
import com.truckintransit.operator.pojo.driverDetails.ResponseFromServerDriverDetails
import com.truckintransit.operator.pojo.driverlist.ResponseFromServerDriverList
import com.truckintransit.operator.pojo.endtrip.ResponseForEndTrip
import com.truckintransit.operator.pojo.otpVerify.ResponseFromServerOtpVerify
import com.truckintransit.operator.pojo.vehicleDetailList.ResponseFromServerVehicleDetailList
import com.truckintransit.operator.pojo.vehicleList.ResponseFromServerVehicleList
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiRequestEndPoint {

    @FormUrlEncoded
    @POST("Api/login")
    fun postServerUserPhoneNumber(@Field("phone") phone: String,@Field("fcm_id") fcm_id: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/driver_detail")
    fun postServerDriverBasiCDetails(@Field("driver_id") phone: String): Observable<ResponseFromServerDriverDetails>
    @FormUrlEncoded
    @POST("Api/update_adhardriver")
    fun editDriverAdharNumber(@Field("driver_id") driver_id: String,
                              @Field("adhar_no") adhar_no: String
                              ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/vehicle_detail")
    fun postServerVehicleBasiCDetails(@Field("vehicle_id") phone: String): Observable<ResponseFromServerVehicleDetailList>

    @FormUrlEncoded
    @POST("Api/update_dldriver")
    fun editDriverDlNumber(@Field("driver_id") driver_id: String,
                              @Field("dl_number") dl_number: String,
                              @Field("expiry_date") expiry_date: String
    ): Observable<ResponseFromServerGeneric>
    @FormUrlEncoded
    @POST("Api/admindashboard")
    fun postServerAdmin(@Field("id") phone: String): Observable<ResponseFromServerAdminDashBoard>

    @FormUrlEncoded
    @POST("Api/driverdashboard")
    fun postServerDriverDashBoard(@Field("id") phone: String): Observable<ResponseFromServerDriverDshBoard>

    @FormUrlEncoded
    @POST("Api/SetpasswordDriver")
    fun updatePasswordDriver(@Field("driver_id") driver_id: String,@Field("password") password: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/SetpasswordAdmin")
    fun updatePasswordAdmin(@Field("id") driver_id: String,@Field("password") password: String,
                            @Field("name") name: String,
                            @Field("email") email: String,
                            @Field("mobile") mobile: String): Observable<ResponseFromServerGeneric>


    @FormUrlEncoded
    @POST("Api/list_driver")
    fun postServerListOfDriver(@Field("admin_id") admin_id: String): Observable<ResponseFromServerDriverList>

    @FormUrlEncoded
    @POST("Api/listactivedriver")
    fun postServerListOfActiveDriver(@Field("admin_id") admin_id: String): Observable<ResponseFromServerDriverList>

    @FormUrlEncoded
    @POST("Api/Vehicleassigned")
    fun assignDriver(@Field("driver_id") driver_id: String,@Field("vehicle_id") vehicle_id: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/vehicle_list")
    fun postServerListOfVehicle(@Field("admin_id") admin_id: String): Observable<ResponseFromServerVehicleList>

    @FormUrlEncoded
    @POST("Api/otpcheck")
    fun postServerUserOtpVerify(@Field("otp") otp: String): Observable<ResponseFromServerOtpVerify>

    @FormUrlEncoded
    @POST("Api/loginwithpassword")
    fun postLogin(@Field("mobile") mobile: String,@Field("password") password: String): Observable<ResponseFromServerOtpVerify>

    @FormUrlEncoded
    @POST("Api/transport_detail")
    fun postServerTransportRegistrstion(@Field("user_id") user_id: String,
                                        @Field("trans_name") trans_name: String,
                                        @Field("trans_gst") trans_gst: String,
                                        @Field("trans_addr") trans_addr: String,
                                        @Field("trans_regno") trans_regno: String,
                                        @Field("type") type: String
                                        ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_latlong")
    fun postServerUserLatLong(@Field("driver_id") user_id: String,
                                        @Field("lat") trans_name: String,
                                        @Field("longg") trans_gst: String,
                                        @Field("isonline") trans_addr: String

    ): Observable<ResponseFromServerGeneric>
    @FormUrlEncoded
    @POST("Api/bank_detail")
    fun postServerBankRegistrstion(@Field("user_id") user_id: String,
                                        @Field("adhar") adhar: String,
                                        @Field("pan") pan: String,
                                        @Field("acc_no") acc_no: String,
                                        @Field("ifsc") ifsc: String,
                                        @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>
    @FormUrlEncoded
    @POST("Api/add_vehiclefasttap")
    fun postServerVehicleFastTagNumber(@Field("admin_id") admin_id: String,
                                   @Field("vehicle_id") vehicle_id: String,
                                   @Field("fasttapno") fasttapno: String,
                                   @Field("type") type: String

    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_vehiclefasttap")
    fun editServerVehicleFastTagNumber(@Field("vehicle_id") vehicle_id: String,
                                       @Field("fasttapno") fasttapno: String,
                                       @Field("type") type: String

    ): Observable<ResponseFromServerGeneric>
    @FormUrlEncoded
    @POST("Api/add_driver")
    fun postAddDriver(@Field("admin_id") user_id: String,
                                   @Field("name") adhar: String,
                                   @Field("mobile") mobile: String,
                                   @Field("email") email: String,
                                   @Field("city_id") city_id: String,
                                   @Field("city_name") city_name: String,
                                   @Field("type") type: String
    ): Observable<ResponseFromServerForDriverBasicDetails>

    @FormUrlEncoded
    @POST("Api/update_driver")
    fun postUdtaeDriver(@Field("driver_id") user_id: String,
                      @Field("name") adhar: String,
                      @Field("mobile") mobile: String,
                      @Field("email") email: String,
                      @Field("city_id") city_id: String,
                      @Field("city_name") city_name: String,
                      @Field("driver_field_status") type: String
    ): Observable<ResponseFromServerGeneric>


    @Multipart
    @POST("Api/user")
    fun uploadImage(@Part image: MultipartBody.Part,
                    @Part("user_id") user_id: RequestBody,
                    @Part("name") name: RequestBody,
                    @Part("email") email: RequestBody,
                    @Part("pass") pass: RequestBody,
                    @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/add_vehicle")
    fun uploadVehicleBasicDetails(@Part image: MultipartBody.Part,
                    @Part("admin_id") admin_id: RequestBody,
                    @Part("brand") brand: RequestBody,
                    @Part("model") model: RequestBody,
                    @Part("reg_no") reg_no: RequestBody,
                    @Part("capacity") capacity: RequestBody,
                    @Part("color") color: RequestBody,
                    @Part("size") size: RequestBody,
                    @Part("body") body: RequestBody,
                    @Part("fuel_type") fuel_type: RequestBody,
                    @Part("select_vehicle") select_vehicle: RequestBody,
                                  @Part("type") type: RequestBody,
                                  @Part("capacity_name") capacity_name: RequestBody,
                                  @Part("body_name") body_name: RequestBody,
                                  @Part("fuel_name") fuel_name: RequestBody,
                                  @Part("select_vehicle_name") select_vehicle_name: RequestBody
    ): Observable<ResponseFromServerAddVehicle>

    @FormUrlEncoded
    @POST("Api/update_vehicle")
    fun editVehicleBasicDetails(@Field("vehicle_id") vehicle_id: String,
                                  @Field("brand") brand: String,
                                  @Field("model") model: String,
                                  @Field("reg_no") reg_no: String,
                                  @Field("capacity") capacity: String,
                                  @Field("color") color: String,
                                  @Field("size") size: String,
                                  @Field("body") body: String,
                                  @Field("fuel_type") fuel_type: String,
                                  @Field("select_vehicle") select_vehicle: String,
                                  @Field("type") type: String,
                                  @Field("capacity_name") capacity_name: String,
                                  @Field("body_name") body_name: String,
                                  @Field("fuel_name") fuel_name: String,
                                  @Field("select_vehicle_name") select_vehicle_name: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_vehicleinsurance")
    fun editImageForVehicleInsurance(@Field("vehicle_id") vehicle_id: String,
                                       @Field("company_name") company_name: String,
                                       @Field("date") date: String,
                                       @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/StartRide")
    fun startRidee(@Field("book_id") book_id: String,
                                     @Field("start_otp") start_otp: String

    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/RideCalculation")
    fun endRidee(@Field("book_id") book_id: String


    ): Observable<ResponseForEndTrip>
    @FormUrlEncoded
    @POST("Api/update_vehiclerc")
    fun editImageForVehicleRc(@Field("vehicle_id") vehicle_id: String,
                                @Field("rc_no") rc_no: String,
                                @Field("date") date: String,
                                @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_vehiclepuc")
    fun uploadImageForVehiclePuc(@Field("vehicle_id") vehicle_id: String,
                                 @Field("validupto") date: String,
                                 @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_vehiclepermit")
    fun editImageForVehiclePermitt(@Field("vehicle_id") vehicle_id: String,
                                     @Field("permit_no") company_name: String,
                                     @Field("validupto") date: String,
                                     @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/update_vehiclefitness")
    fun editImageForVehicleFitness(@Field("vehicle_id") vehicle_id: String,
                                     @Field("fitnessno") company_name: String,
                                     @Field("validupto") date: String,
                                     @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Driversttaus")
    fun driverBlocked(@Field("driver_id") driver_id: String,
                                   @Field("status") status: String

    ): Observable<ResponseFromServerGeneric>
    @Multipart
    @POST("Api/driver_adharpic")
    fun uploadImageAdhar(@Part image: MultipartBody.Part,@Part image1: MultipartBody.Part,
                    @Part("user_id") user_id: RequestBody,
                    @Part("adhaar_no") adhaar_no: RequestBody, @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>
    @Multipart
    @POST("Api/updatedriver_adharpic")
    fun editImageAdhar(@Part image: MultipartBody.Part,   @Part("pic_status") pic_status: RequestBody,
                         @Part("driver_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehiclephoto")
    fun editImageVehicleProfile(@Part image: MultipartBody.Part,
                       @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehicleinsurancephoto")
    fun editImageVehicleInsurance(@Part image: MultipartBody.Part,
                                @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehiclepermitphoto")
    fun editImageVehiclePermit(@Part image: MultipartBody.Part,
                                  @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehiclepucphoto")
    fun editImageVehiclePuc(@Part image: MultipartBody.Part,
                               @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehiclefitnessphoto")
    fun editImageVehicleFiness(@Part image: MultipartBody.Part,
                            @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/update_vehiclercphoto")
    fun editImageVehicleRc(@Part image: MultipartBody.Part,
                               @Part("vehicle_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>
    @Multipart
    @POST("Api/driver_dlpic")
    fun uploadImageDl(@Part image: MultipartBody.Part,@Part image1: MultipartBody.Part,
                         @Part("user_id") user_id: RequestBody,
                         @Part("expiry_date") expiry_date: RequestBody,
                         @Part("dl_no") dl_no: RequestBody, @Part("type") type: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/updatedriver_dlpic")
    fun editImageDl(@Part image: MultipartBody.Part, @Part("pic_status") pic_status: RequestBody,
                      @Part("driver_id") driver_id: RequestBody


    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/driver_profilepic")
    fun uploadImageForProfilePic(@Part image: MultipartBody.Part,
                    @Part("user_id") user_id: RequestBody,
                                 @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/updatedriver_profilepic")
    fun editImageForProfilePic(@Part image: MultipartBody.Part,
                                 @Part("driver_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>


    @Multipart
    @POST("Api/driver_addresspic")
    fun uploadImageForAdressPhoto(@Part image: MultipartBody.Part,
                                 @Part("user_id") user_id: RequestBody,
                                 @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>
    @Multipart
    @POST("Api/add_vehiclerc")
    fun uploadImageForVehicleRc(@Part image: MultipartBody.Part,
                                  @Part("admin_id") admin_id: RequestBody,
                                  @Part("vehicle_id") vehicle_id: RequestBody,
                                  @Part("rc_no") rc_no: RequestBody,
                                  @Part("date") date: RequestBody,
                                  @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/add_vehicleinsurance")
    fun uploadImageForVehicleInsurance(@Part image: MultipartBody.Part,
                                @Part("admin_id") admin_id: RequestBody,
                                @Part("vehicle_id") vehicle_id: RequestBody,
                                @Part("company_name") company_name: RequestBody,
                                @Part("date") date: RequestBody,
                                @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/add_vehiclepermit")
    fun uploadImageForVehiclePermitt(@Part image: MultipartBody.Part,
                                       @Part("admin_id") admin_id: RequestBody,
                                       @Part("vehicle_id") vehicle_id: RequestBody,
                                       @Part("permit_no") company_name: RequestBody,
                                       @Part("validupto") date: RequestBody,
                                       @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>
    @Multipart
    @POST("Api/add_vehiclefitness")
    fun uploadImageForVehicleFitness(@Part image: MultipartBody.Part,
                                     @Part("admin_id") admin_id: RequestBody,
                                     @Part("vehicle_id") vehicle_id: RequestBody,
                                     @Part("fitnessno") company_name: RequestBody,
                                     @Part("validupto") date: RequestBody,
                                     @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/add_vehiclepuc")
    fun uploadImageForVehiclePuc(@Part image: MultipartBody.Part,
                                     @Part("admin_id") admin_id: RequestBody,
                                     @Part("vehicle_id") vehicle_id: RequestBody,
                                     @Part("validupto") date: RequestBody,
                                     @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>



    @Multipart
    @POST("Api/updatedriver_addressphoto")
    fun editImageForAdressPhoto(@Part image: MultipartBody.Part,
                                  @Part("driver_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>
    @GET("Api/city")
    fun getCityList(): Observable<ResponseFromServerCityList>


    @GET("Api/capacity")
    fun getCapicityList(): Observable<ResponseForCatogoryDialog>

    @GET("Api/body_type")
    fun getBodyList(): Observable<ResponseForCatogoryDialog>

    @GET("Api/fuel_type")
    fun getFuelList(): Observable<ResponseForCatogoryDialog>

    @GET("Api/select_vehicle")
    fun getVehicleList(): Observable<ResponseForCatogoryDialog>

    @FormUrlEncoded
    @POST("http://technowhizzit.com/Truckintransit/Api/booking")
    fun postServerBookingAccept(@Field("customer_id") customer_id: String,
                          @Field("driver_id") driver_id: String,
                          @Field("serviceid") serviceid: String,
                          @Field("pickup_loc") pickup_loc: String,
                          @Field("pickup_lat") pickup_lat: String,
                          @Field("pickup_longg") pickup_longg: String,
                          @Field("pickup2") pickup2: String,
                          @Field("pickup2_lat") pickup2_lat: String,
                          @Field("pickup2_longgg") pickup2_longgg: String,
                          @Field("pickup3") pickup3: String,
                          @Field("pickup3_lat") pickup3_lat: String,
                          @Field("pickup3_longg") pickup3_longg: String,
                          @Field("drop_loc") drop_loc: String,
                          @Field("drop_lat") drop_lat: String,
                          @Field("drop_longg") drop_longg: String,
                          @Field("dropoff2") dropoff2: String,
                          @Field("drop2_lat") drop2_lat: String,
                          @Field("drop2_longg") drop2_longg: String,
                          @Field("dropoff3") dropoff3: String,
                          @Field("drop3_lat") drop3_lat: String,
                          @Field("drop3_longg") drop3_longg: String,
                          @Field("truckid") truckid: String,
                          @Field("optionshelper") optionshelper: String,
                          @Field("vehiclebodytype") vehiclebodytype: String,
                          @Field("vehiclefueltypeid") vehiclefueltypeid: String,
                          @Field("vehicletypeid") vehicletypeid: String,
                          @Field("material_type") material_type: String,
                          @Field("coupon") coupon: String,
                          @Field("freightpayment") freightpayment: String,
                          @Field("payment_mode") payment_mode: String,
                          @Field("titcredits") titcredits: String
    ): Observable<ResponseBookingAccept>

    @FormUrlEncoded
    @POST("http://technowhizzit.com/Truckintransit/Api/booking")
    fun postServerBooking(@Field("customer_id") customer_id: String,
                          @Field("driver_id") driver_id: String,
                          @Field("serviceid") serviceid: String,
                          @Field("pickup_loc") pickup_loc: String,
                          @Field("pickup_lat") pickup_lat: String,
                          @Field("pickup_longg") pickup_longg: String,
                          @Field("pickup2") pickup2: String,
                          @Field("pickup2_lat") pickup2_lat: String,
                          @Field("pickup2_longgg") pickup2_longgg: String,
                          @Field("pickup3") pickup3: String,
                          @Field("pickup3_lat") pickup3_lat: String,
                          @Field("pickup3_longg") pickup3_longg: String,
                          @Field("drop_loc") drop_loc: String,
                          @Field("drop_lat") drop_lat: String,
                          @Field("drop_longg") drop_longg: String,
                          @Field("dropoff2") dropoff2: String,
                          @Field("drop2_lat") drop2_lat: String,
                          @Field("drop2_longg") drop2_longg: String,
                          @Field("dropoff3") dropoff3: String,
                          @Field("drop3_lat") drop3_lat: String,
                          @Field("drop3_longg") drop3_longg: String,
                          @Field("truckid") truckid: String,
                          @Field("optionshelper") optionshelper: String,
                          @Field("vehiclebodytype") vehiclebodytype: String,
                          @Field("vehiclefueltypeid") vehiclefueltypeid: String,
                          @Field("vehicletypeid") vehicletypeid: String,
                          @Field("material_type") material_type: String,
                          @Field("coupon") coupon: String,
                          @Field("freightpayment") freightpayment: String,
                          @Field("payment_mode") payment_mode: String,
                          @Field("titcredits") titcredits: String
    ): Observable<ResponseFromServerGeneric>
}