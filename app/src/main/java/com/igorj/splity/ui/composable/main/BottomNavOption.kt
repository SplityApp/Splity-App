import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import com.igorj.splity.ui.theme.localColorScheme

@Composable
fun BottomNavOption(
    modifier: Modifier = Modifier
    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(localColorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "BottomNavOption",
            color = localColorScheme.onBackground
        )
    }
}