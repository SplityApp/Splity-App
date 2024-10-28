
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.igorj.splity.model.main.BottomNavItem


@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BottomNavOption(
                modifier = Modifier.weight(1f),
                bottomNavItem = item,
                currentRoute = currentRoute,
                onClick = onNavigate
            )
        }
    }
}