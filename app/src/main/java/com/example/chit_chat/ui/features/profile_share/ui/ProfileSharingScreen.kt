package com.example.chit_chat.ui.features.profile_share.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chit_chat.R
import com.example.chit_chat.ui.features.profile_share.view_model.ProfileSharingViewModel
import com.example.chit_chat.utils.getQrCodeBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSharingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileSharingScreen(
                    onBackClickListener = {
                        this@ProfileSharingFragment.findNavController().popBackStack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileSharingScreen(
    viewModel: ProfileSharingViewModel = hiltViewModel(),
    onBackClickListener: () -> Unit
) {
    val context = LocalContext.current
    val profile = viewModel.sharingProfile.collectAsState()
    val launchKey = remember { mutableStateOf(true) }

    LaunchedEffect(launchKey) {
        viewModel.getProfileInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.sharing_profile_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClickListener.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
        val scaffoldPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = 16.dp,
            start = padding.calculateStartPadding(LayoutDirection.Ltr) + 24.dp,
            end = padding.calculateEndPadding(LayoutDirection.Ltr) + 24.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = profile.value.avatarUrl,
                contentDescription = "Avatar",
                loading = placeholder(R.drawable.ic_round_avatar_placeholder),
                failure = placeholder(R.drawable.ic_round_avatar_placeholder),
                modifier = Modifier
                    .widthIn(0.dp, 128.dp)
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.sharing_text_field_qr)
            )

            Image(
                bitmap = getQrCodeBitmap(profile.value.id, context).asImageBitmap(),
                contentDescription = "QR code",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .widthIn(0.dp, 256.dp)
                    .aspectRatio(1f)
                    .border(1.dp, Color.LightGray)
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.sharing_or)
            )
            Text(
                modifier = Modifier.padding(),
                text = stringResource(id = R.string.sharing_text_field_username)
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile.value.id,
                    modifier = Modifier.padding(start = 8.dp)
                )
                IconButton(onClick = {
                    val sharingText = SHARING_TEXT + profile.value.id
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sharingText)
                    shareIntent.type = SEND_INTENT_TYPE

                    val chooserIntent = Intent.createChooser(shareIntent, null)
                    startActivity(context, chooserIntent, null)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}

private const val SEND_INTENT_TYPE = "text/plain"
private const val SHARING_TEXT = "Привет! Мой аккаунт в Chit Chat: https://chitchat-app-by-kotez.web.app/add_contact?id="