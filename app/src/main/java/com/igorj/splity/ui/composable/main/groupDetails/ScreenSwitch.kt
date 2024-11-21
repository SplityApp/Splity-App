
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.composable.main.groupDetails.GroupDetailsScreen
import com.igorj.splity.ui.theme.localColorScheme

@Composable
fun ScreenSwitch(
    leftScreen: @Composable () -> Unit,
    rightScreen: @Composable () -> Unit,
    leftLabel: String,
    rightLabel: String,
    leftIcon: ImageVector,
    rightIcon: ImageVector,
    selectedOption: ScreenSwitchOption,
    onSwitch: (ScreenSwitchOption) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(localColorScheme.surface)
                .height(40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    onSwitch(ScreenSwitchOption.First)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == ScreenSwitchOption.First) {
                        localColorScheme.primary
                    } else {
                        localColorScheme.surface
                    },
                    contentColor = if (selectedOption == ScreenSwitchOption.First) {
                        localColorScheme.onPrimary
                    } else {
                        localColorScheme.onSurfaceVariant
                    }
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
                        contentDescription = leftLabel,
                        modifier = Modifier.size(16.dp),
                        tint = if (selectedOption == ScreenSwitchOption.First) {
                            localColorScheme.onPrimary
                        } else {
                            localColorScheme.onSurfaceVariant
                        }
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
                    onSwitch(ScreenSwitchOption.Second)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == ScreenSwitchOption.Second) {
                        localColorScheme.primary
                    } else {
                        localColorScheme.surface
                    },
                    contentColor = if (selectedOption == ScreenSwitchOption.Second) {
                        localColorScheme.onPrimary
                    } else {
                        localColorScheme.onSurfaceVariant
                    }
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
                        contentDescription = rightLabel,
                        modifier = Modifier.size(16.dp),
                        tint = if (selectedOption == ScreenSwitchOption.Second) {
                            localColorScheme.onPrimary
                        } else {
                            localColorScheme.onSurfaceVariant
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rightLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedOption == ScreenSwitchOption.First) {
                leftScreen()
            } else {
                rightScreen()
            }
        }
    }
}

enum class ScreenSwitchOption {
    First, Second
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
        selectedOption = ScreenSwitchOption.First,
        rightIcon = Icons.Default.FavoriteBorder
    )
}
