package alektas.gamesheap.di

import alektas.gamesheap.R
import alektas.gamesheap.data.DataSource
import alektas.gamesheap.data.GamesRepository
import alektas.gamesheap.data.remote.RemoteGamesSource
import alektas.gamesheap.domain.Repository
import alektas.gamesheap.data.remote.api.GamesApi
import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideRepository(remoteSource: DataSource): Repository {
        return GamesRepository(remoteSource)
    }

    @Provides
    @Singleton
    fun provideRemoteGamesSource(context: Application): DataSource {
        return RemoteGamesSource(context.resources.getString(R.string.api_key))
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.giantbomb.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    fun provideGamesService(retrofit: Retrofit): GamesApi {
        return retrofit.create(GamesApi::class.java)
    }

}