package br.com.dio.coinconverter.domain.di

import android.util.Log
import br.com.dio.coinconverter.data.repository.CoinRepository
import br.com.dio.coinconverter.data.repository.CoinRepositoryImpl
import br.com.dio.coinconverter.data.services.AwesomeService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter.Companion.factory
import okhttp3.internal.threadFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.create
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModules {
    private const val HTTP_TAG = "OhHttp"

    fun load(){
        loadKoinModules(networkModule() + repositoryModules())
    }
    private fun networkModule(): Module {
        return module {
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.e(HTTP_TAG, ": $it")

                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            }
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                createService<AwesomeService>(get(), get())

            }
        }
    }
    private fun repositoryModules(): Module {
        return module {
            single<CoinRepository> { CoinRepositoryImpl(get()) }
        }
    }
    private inline fun <reified T> createService(client: OkHttpClient, factory: GsonConverterFactory): T{
        return Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br")
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}