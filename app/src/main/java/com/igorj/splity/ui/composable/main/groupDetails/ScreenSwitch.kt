import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.composable.main.groupDetails.GroupDetailsScreen
import com.igorj.splity.ui.theme.localColorScheme

enum class Option { FIRST, SECOND }

@Composable
fun ScreenSwitch(
    leftScreen: @Composable () -> Unit,
    rightScreen: @Composable () -> Unit,
    leftLabel: String,
    rightLabel: String,
    leftIcon: ImageVector,
    rightIcon: ImageVector,
    onSwitch: (() -> Unit)? = null
) {
    var selectedOption by remember { mutableStateOf(Option.FIRST) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Switch buttons in a more compact container
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(localColorScheme.surface)
                .height(40.dp), // Fixed height for the switch
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    selectedOption = Option.FIRST
                    onSwitch?.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == Option.FIRST)
                        localColorScheme.primary
                    else
                        localColorScheme.surface,
                    contentColor = if (selectedOption == Option.FIRST)
                        localColorScheme.onPrimary
                    else
                        localColorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = leftIcon,
                        contentDescription = "leftIcon",
                        modifier = Modifier.size(16.dp),
                        tint = if (selectedOption == Option.FIRST)
                            localColorScheme.onPrimary
                        else
                            localColorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = leftLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Button(
                onClick = {
                    selectedOption = Option.SECOND
                    onSwitch?.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == Option.SECOND)
                        localColorScheme.primary
                    else
                        localColorScheme.surface,
                    contentColor = if (selectedOption == Option.SECOND)
                        localColorScheme.onPrimary
                    else
                        localColorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = rightIcon,
                        contentDescription = "rightIcon",
                        modifier = Modifier.size(16.dp),
                        tint = if (selectedOption == Option.SECOND)
                            localColorScheme.onPrimary
                        else
                            localColorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rightLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Content
        Box(modifier = Modifier.weight(1f)) {
            if (selectedOption == Option.FIRST) {
                leftScreen()
            } else {
                rightScreen()
            }
        }
    }
}

@Preview
@Composable
fun ScreenSwitchPreview() {
    ScreenSwitch(
        leftScreen = {
            GroupDetailsScreen(groupId = "1")
        },
        rightScreen = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            )
        },
        leftLabel = "First",
        rightLabel = "Second",
        leftIcon = Icons.Default.Favorite,
        rightIcon = Icons.Default.FavoriteBorder
    )
}
