
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.model.main.BottomNavItem
import com.igorj.splity.ui.theme.localColorScheme

@Composable
fun BottomNavOption(
    modifier: Modifier = Modifier,
    bottomNavItem: BottomNavItem,
    currentRoute: String,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(localColorScheme.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick(bottomNavItem.route)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = bottomNavItem.icon,
            contentDescription = stringResource(bottomNavItem.label),
            tint = if (currentRoute == bottomNavItem.route) localColorScheme.primary else localColorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun BottomNavOptionPreview() {
    BottomNavOption(
        bottomNavItem = BottomNavItem.Home,
        currentRoute = "Home"
    )
}