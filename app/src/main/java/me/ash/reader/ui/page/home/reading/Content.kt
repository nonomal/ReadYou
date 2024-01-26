package me.ash.reader.ui.page.home.reading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.ash.reader.infrastructure.preference.LocalOpenLink
import me.ash.reader.infrastructure.preference.LocalOpenLinkSpecificBrowser
import me.ash.reader.infrastructure.preference.LocalReadingRenderer
import me.ash.reader.infrastructure.preference.LocalReadingSubheadUpperCase
import me.ash.reader.infrastructure.preference.ReadingRendererPreference
import me.ash.reader.ui.component.base.RYExtensibleVisibility
import me.ash.reader.ui.component.base.RYWebView
import me.ash.reader.ui.component.reader.Reader
import me.ash.reader.ui.ext.drawVerticalScrollbar
import me.ash.reader.ui.ext.openURL
import java.util.Date

@Composable
fun Content(
    content: String,
    feedName: String,
    title: String,
    author: String? = null,
    link: String? = null,
    publishedDate: Date,
    listState: LazyListState,
    isLoading: Boolean,
    onImageClick: ((imgUrl: String, altText: String) -> Unit)? = null,
) {
    val context = LocalContext.current
    val renderer = LocalReadingRenderer.current
    val subheadUpperCase = LocalReadingSubheadUpperCase.current
    val openLink = LocalOpenLink.current
    val openLinkSpecificBrowser = LocalOpenLinkSpecificBrowser.current

    SelectionContainer {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .drawVerticalScrollbar(listState),
            state = listState,
        ) {
            item {
                // Top bar height
                Spacer(modifier = Modifier.height(64.dp))
                // padding
                Spacer(modifier = Modifier.height(22.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    DisableSelection {
                        Metadata(
                            feedName = feedName,
                            title = title,
                            author = author,
                            link = link,
                            publishedDate = publishedDate,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(22.dp))
                RYExtensibleVisibility(visible = isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(22.dp))
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.height(22.dp))
                        }
                    }
                }
            }
            if (!isLoading) {
                when (renderer) {
                    ReadingRendererPreference.WebView ->
                        item { RYWebView(content = content) }

                    ReadingRendererPreference.NativeComponent ->
                        Reader(
                            context = context,
                            subheadUpperCase = subheadUpperCase.value,
                            link = link ?: "",
                            content = content,
                            onLinkClick = {
                                context.openURL(it, openLink, openLinkSpecificBrowser)
                            }
                        )
                }
            }
            item {
                Spacer(modifier = Modifier.height(128.dp))
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}
