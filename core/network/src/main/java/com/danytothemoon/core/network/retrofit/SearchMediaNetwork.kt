package com.danytothemoon.core.network.retrofit

import com.danytothemoon.core.network.BuildConfig
import com.danytothemoon.core.network.model.SearchImageResult
import com.danytothemoon.core.network.model.SearchVideoResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface SearchMediaApi {
  @GET("vclip")
  suspend fun getVideos(
    @Query("query") query: String,
    @Query("sort") sort: String,
    @Query("page") page: Int,
    @Query("size") size: Int,
    @Header("Authorization") authorization: String = BuildConfig.KAKAO_API_KEY,
  ): SearchVideoResult

  @GET("image")
  suspend fun getImages(
    @Query("query") query: String,
    @Query("sort") sort: String,
    @Query("page") page: Int,
    @Query("size") size: Int,
    @Header("Authorization") authorization: String = BuildConfig.KAKAO_API_KEY,
  ): SearchImageResult
}

@Singleton
class SearchMediaNetwork @Inject constructor(networkJson: Json) {
  private val api = Retrofit.Builder()
    .baseUrl(BuildConfig.KAKAO_API_BASE_URL)
    .addConverterFactory(
      @OptIn(ExperimentalSerializationApi::class)
      networkJson.asConverterFactory(MediaType.parse("application/json")!!)
    )
    .build()
    .create(SearchMediaApi::class.java)

  /**
   * @param query : 검색을 원하는 질의어
   * @param sort : 결과 문서 정렬 방식. accuracy (정확도순), recency (최신순)
   * @param page : 결과 페이지 번호, 1 ~ 15 사이의 값
   * @param size : 한 페이지에 보여질 문서 수, 1 ~ 30 사이의 값
   */
  suspend fun getVideos(
    query: String,
    sort: String = "accuracy",
    page: Int = 1,
    size: Int = 15,
  ): SearchVideoResult = api.getVideos(query, sort, page, size)

  /**
   * @param query : 검색을 원하는 질의어
   * @param sort : 결과 문서 정렬 방식. accuracy (정확도순), recency (최신순)
   * @param page : 결과 페이지 번호, 1 ~ 50 사이의 값
   * @param size : 한 페이지에 보여질 문서 수, 1 ~ 80 사이의 값
   */
  suspend fun getImages(
    query: String,
    sort: String = "accuracy",
    page: Int = 1,
    size: Int = 80,
  ): SearchImageResult = api.getImages(query, sort, page, size)
}