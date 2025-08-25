package com.nutriweek.inclusiverecip
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nutriweek.inclusiverecip.core.theme.AccessRecetasTheme
import com.nutriweek.inclusiverecip.ui.Routes
import com.nutriweek.inclusiverecip.ui.screens.auth.LoginScreen
import com.nutriweek.inclusiverecip.ui.screens.recipes.PlanScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}


@Composable
fun App() {
    AccessRecetasTheme {
        Surface { AppNav() }
    }
}


@Composable
fun AppNav(nav: NavHostController = rememberNavController()) {
    NavHost(navController = nav, startDestination = Routes.Login) {
        composable(Routes.Login) {
            LoginScreen(
                onGoRegister = { /* nav.navigate(Routes.Register) (próximo paso) */ },
                onGoRecover = { /* nav.navigate(Routes.Recover) (próximo paso) */ },
                onSuccess = { nav.navigate(Routes.Plan) { popUpTo(Routes.Login) { inclusive = true } } }
            )
        }
        composable(Routes.Plan) { PlanScreen() }
    }
}