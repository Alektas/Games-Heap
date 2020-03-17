package alektas.gamesheap.di

import alektas.gamesheap.common.ui.ActivityViewModel
import alektas.gamesheap.data.remote.RemoteGamesSource
import alektas.gamesheap.gamelist.ui.GamelistViewModel
import alektas.gamesheap.filter.ui.FiltersViewModel
import alektas.gamesheap.gamedetails.ui.GameViewModel
import alektas.gamesheap.searchlist.ui.SearchViewModel
import dagger.Component
import android.app.Application
import dagger.BindsInstance
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injects(source: RemoteGamesSource)
    fun injects(viewModel: GamelistViewModel)
    fun injects(viewModel: SearchViewModel)
    fun injects(viewModel: FiltersViewModel)
    fun injects(viewModel: GameViewModel)
    fun injects(viewModel: ActivityViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}