package com.soft.template.di

import com.soft.template.BuildConfig
import com.soft.template.data.api.ApiService
import com.soft.template.data.repository.MainRepository
import com.soft.template.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModules {
    val Project = module {
        single { providerApiEvents() }
        single { MainRepository(get()) }
        viewModel { MainViewModel() }
    }

    private fun providerApiEvents(): ApiService {

        val client = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)


        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        client.addInterceptor(logger)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(ApiService::class.java)
    }
}