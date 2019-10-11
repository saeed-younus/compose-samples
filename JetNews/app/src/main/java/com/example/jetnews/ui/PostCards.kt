/*
 * Copyright 2019 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetnews.ui

import androidx.compose.Composable
import androidx.compose.composer
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.selection.Toggleable
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.CrossAxisAlignment
import androidx.ui.layout.FlexRow
import androidx.ui.layout.LayoutSize
import androidx.ui.layout.MainAxisAlignment
import androidx.ui.layout.Padding
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.themeTextStyle
import androidx.ui.material.withOpacity

@Composable
fun AuthorAndReadTime(post: Post) {
    FlexRow {
        val textStyle = (+themeTextStyle { body2 }).withOpacity(0.6f)
        flexible(1f) {
            Text(text = post.metadata.author.name, style = textStyle)
        }
        inflexible {
            Text(
                text = " - ${post.metadata.readTimeMinutes} min read",
                style = textStyle
            )
        }
    }
}

@Composable
fun PostImage(post: Post, icons: Icons) {
    val image = post.imageThumb ?: icons.placeholder_1_1

    Container(width = 40.dp, height = 40.dp) {
        DrawImage(image)
    }
}

@Composable
fun PostTitle(post: Post) {
    Text(post.title, style = (+themeTextStyle { subtitle1 }).withOpacity(0.87f))
}

@Composable
fun PostCardSimple(post: Post, icons: Icons) {
    Ripple(bounded = true) {
        Clickable(onClick = {
            navigateTo(Screen.Article(post.id))
        }) {
            Padding(16.dp) {
                FlexRow(crossAxisAlignment = CrossAxisAlignment.Start) {
                    inflexible {
                        Padding(right = 16.dp) {
                            PostImage(post, icons)
                        }
                    }
                    flexible(1f) {
                        Column(mainAxisSize = LayoutSize.Expand) {
                            PostTitle(post)
                            AuthorAndReadTime(post)
                        }
                    }
                    inflexible {
                        BookmarkButton(post, icons)
                    }
                }
            }
        }
    }
}

@Composable
fun PostCardHistory(post: Post, icons: Icons) {
    Ripple(bounded = true) {
        Clickable(onClick = {
            navigateTo(Screen.Article(post.id))
        }) {
            Padding(16.dp) {
                FlexRow(crossAxisAlignment = CrossAxisAlignment.Start) {
                    inflexible {
                        Padding(right = 16.dp) {
                            PostImage(post = post, icons = icons)
                        }
                    }
                    flexible(1f) {
                        Column(
                            crossAxisAlignment = CrossAxisAlignment.Start,
                            crossAxisSize = LayoutSize.Wrap,
                            mainAxisAlignment = MainAxisAlignment.Start,
                            mainAxisSize = LayoutSize.Expand
                        ) {
                            Text(
                                text = "BASED ON YOUR HISTORY",
                                style = (+themeTextStyle { overline }).withOpacity(0.38f)
                            )
                            PostTitle(post = post)
                            AuthorAndReadTime(post)
                        }
                    }
                    inflexible {
                        Padding(top = 8.dp, bottom = 8.dp) {
                            SimpleImage(icons.more)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarkButton(post: Post, icons: Icons) {
    val isFav = isFavorite(post.id)
    Ripple(
        bounded = false,
        radius = 24.dp
    ) {
        Toggleable(
            checked = isFav,
            onCheckedChange = { toggleBookmark(post.id) }
        ) {
            Container(width = 48.dp, height = 48.dp) {
                SimpleImage(if (isFav) icons.bookmarkOn else icons.bookmarkOff)
            }
        }
    }
}

fun toggleBookmark(postId: String) {
    JetnewsStatus.run {
        if (favorites.contains(postId)) {
            favorites.remove(postId)
        } else {
            favorites.add(postId)
        }
    }
}

fun isFavorite(postId: String): Boolean {
    return JetnewsStatus.favorites.contains(postId)
}
