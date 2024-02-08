package com.sginnovations.asked.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.sginnovations.asked.auth.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.domain.usecase.firebase.setters.SetDefaultTokensUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthUiClient {

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        setDefaultTokensUseCase: SetDefaultTokensUseCase,
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context),
            setDefaultTokensUseCase = setDefaultTokensUseCase,
        )
    }

    /**
     * Executor
     */
    @Provides
    fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()


}
