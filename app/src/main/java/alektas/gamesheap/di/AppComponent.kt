package alektas.gamesheap.di

import alektas.gamesheap.data.remote.RemoteGamesSource
import alektas.gamesheap.ui.gamelist.GamelistViewModel
import alektas.gamesheap.ui.gamelist.filters.FiltersViewModel
import alektas.gamesheap.ui.gamelist.game.GameViewModel
import alektas.gamesheap.ui.search.SearchViewModel
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

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}