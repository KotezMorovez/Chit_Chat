package com.example.chit_chat.ui.features.profile_details.ui

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.example.chit_chat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileDetailsScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
@Preview(showBackground = true)
fun ProfileDetailsScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.details_profile_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
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
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
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
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            val qrCode = getQrCodeBitmap(viewModel.getProfileId(), context)
//            val avatarPlaceholder = Placeholder(
//
//            )


            GlideImage(
                model = painterResource(id = R.drawable.ic_round_avatar_placeholder),/*viewModel.getAvatar()*/
                contentDescription = "Avatar",
//                failure = painterResource(id = R.drawable.ic_round_avatar_placeholder),
                modifier = Modifier
                    .widthIn(0.dp, 256.dp)
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.details_text_field_qr)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_delete) /*qrCode.asImageBitmap()*/,
                contentDescription = "Avatar",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .widthIn(0.dp, 256.dp)
                    .aspectRatio(1f)
                    .border(1.dp, Color.Gray)
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.details_or)
            )
            Text(
                modifier = Modifier.padding(),
                text = stringResource(id = R.string.details_text_field_username)
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "YourUserNameTest123"/*viewModel.getProfileId()*/)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
//                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Copy"
                    )
                }
            }
        }
    }
}