package id.hanzdev.mampat.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import id.hanzdev.mampat.R
import id.hanzdev.mampat.model.CompressorUiState

@Composable
fun InfoDialog(
    state: CompressorUiState,
    onDismiss: (Boolean) -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onToggleShowBitrate: () -> Unit,
    onToggleBitrateUnit: () -> Unit,
    onEnableAllCodecs: () -> Unit,
    isSoftwareCodec: (String) -> Boolean
) {
    var copied by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    var tapCount by remember { mutableIntStateOf(0) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var localAllCodecsEnabled by remember(state.allCodecsEnabled) { mutableStateOf(state.allCodecsEnabled) }

    if (showConfirmDialog) {
        var checked1 by remember { mutableStateOf(false) }
        var checked2 by remember { mutableStateOf(false) }
        var checked3 by remember { mutableStateOf(false) }
        var checked4 by remember { mutableStateOf(false) }
        var checked5 by remember { mutableStateOf(false) }

        val allChecked = checked1 && checked2 && checked3 && checked4 && checked5

        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(stringResource(R.string.are_you_sure)) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        stringResource(R.string.enable_all_codecs_warn_dialog),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    CheckboxRow(
                        checked = checked1,
                        onCheckedChange = { checked1 = it },
                        label = buildAnnotatedString {
                            append("Saya ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("BENCI")
                            }
                            append(" baterai yang awet")
                        }
                    )
                    CheckboxRow(
                        checked = checked2,
                        onCheckedChange = { checked2 = it },
                        label = buildAnnotatedString {
                            append("Saya ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("SUKA")
                            }
                            append(" kalau tangan saya meleleh")
                        }
                    )
                    CheckboxRow(
                        checked = checked3,
                        onCheckedChange = { checked3 = it },
                        label = buildAnnotatedString {
                            append(stringResource(R.string.warn_time))
                        }
                    )
                    CheckboxRow(
                        checked = checked4,
                        onCheckedChange = { checked4 = it },
                        label = buildAnnotatedString {
                            append("Saya ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("SUKA")
                            }
                            append(" kalau aplikasi terus-menerus crash")
                        }
                    )
                    CheckboxRow(
                        checked = checked5,
                        onCheckedChange = { checked5 = it },
                        label = buildAnnotatedString {
                            append("Saya ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("TIDAK AKAN")
                            }
                            append(" membuat laporan bug selama ini aktif")
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = allChecked,
                    onClick = {
                        showConfirmDialog = false
                        onEnableAllCodecs()
                    }
                ) {
                    Text(stringResource(R.string.enable_all_codecs))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = { onDismiss(localAllCodecsEnabled) },
        title = {
            Column {
                 Text(stringResource(R.string.info_title), style = MaterialTheme.typography.titleLarge)
                 Text("${stringResource(R.string.app_name)} v${state.appInfoVersion}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                InfoRow(stringResource(R.string.info_device), "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
                InfoRow(stringResource(R.string.info_android), android.os.Build.VERSION.RELEASE)
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.show_bitrate), style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = state.showBitrate, 
                        onCheckedChange = { onToggleShowBitrate() }
                    )
                }
                
                AnimatedVisibility(visible = state.showBitrate) {
                     Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.bitrate_unit_mbps), 
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Switch(
                            checked = state.useMbps, 
                            onCheckedChange = { onToggleBitrateUnit() }
                        )
                    }
                }

                if (state.allCodecsUnlocked) {
                     Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.enable_all_codecs), 
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(
                            checked = localAllCodecsEnabled, 
                            onCheckedChange = { localAllCodecsEnabled = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.allCodecsUnlocked) {
                                tapCount++
                                if (tapCount >= 7) {
                                    showConfirmDialog = true
                                    tapCount = 0
                                } else if (tapCount >= 4) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.step_away_codecs, 7 - tapCount),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                ) {
                    InfoRow(stringResource(R.string.info_supported_codecs), "")
                }
                state.supportedCodecs.forEach { codec ->
                     val isSoftware = isSoftwareCodec(codec)
                      Row(
                          modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                          verticalAlignment = Alignment.CenterVertically
                      ) {
                         Text(
                             "• ${codec.substringAfter("/")}", 
                             style = MaterialTheme.typography.bodySmall
                         )
                         if (isSoftware) {
                             Spacer(modifier = Modifier.width(4.dp))
                             Icon(
                                 imageVector = Icons.Outlined.Warning,
                                 contentDescription = stringResource(R.string.software_encoding_warning_desc),
                                 tint = Color(0xFFFBC02D), // Yellow
                                 modifier = Modifier.size(12.dp)
                             )
                             Spacer(modifier = Modifier.width(2.dp))
                             Text(
                                 stringResource(R.string.software_encoding),
                                 style = MaterialTheme.typography.bodySmall,
                                 color = Color(0xFFFBC02D)
                             )
                         }
                      }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(
                    onClick = { uriHandler.openUri("mailto:hanzzcodee@gmail.com") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.contact_developer))
                }
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = onShare) {
                    Text(stringResource(R.string.share))
                }
                TextButton(
                    onClick = { 
                        onCopy()
                        copied = true 
                    }
                ) {
                    Text(if (copied) stringResource(R.string.info_copied) else stringResource(R.string.info_copy_clipboard))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(localAllCodecsEnabled) }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
