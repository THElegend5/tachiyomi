package eu.kanade.presentation.manga

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import eu.kanade.domain.chapter.model.Chapter
import eu.kanade.presentation.components.ChapterDownloadAction
import eu.kanade.presentation.components.ExtendedFloatingActionButton
import eu.kanade.presentation.components.LazyColumn
import eu.kanade.presentation.components.MangaBottomActionMenu
import eu.kanade.presentation.components.Scaffold
import eu.kanade.presentation.components.SwipeRefresh
import eu.kanade.presentation.components.TwoPanelBox
import eu.kanade.presentation.components.VerticalFastScroller
import eu.kanade.presentation.manga.components.ChapterHeader
import eu.kanade.presentation.manga.components.ExpandableMangaDescription
import eu.kanade.presentation.manga.components.MangaActionRow
import eu.kanade.presentation.manga.components.MangaChapterListItem
import eu.kanade.presentation.manga.components.MangaInfoBox
import eu.kanade.presentation.manga.components.MangaToolbar
import eu.kanade.presentation.util.isScrolledToEnd
import eu.kanade.presentation.util.isScrollingUp
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.source.SourceManager
import eu.kanade.tachiyomi.source.getNameForMangaInfo
import eu.kanade.tachiyomi.ui.manga.ChapterItem
import eu.kanade.tachiyomi.ui.manga.MangaScreenState

@Composable
fun MangaScreen(
    state: MangaScreenState.Success,
    snackbarHostState: SnackbarHostState,
    isTabletUi: Boolean,
    onBackClicked: () -> Unit,
    onChapterClicked: (Chapter) -> Unit,
    onDownloadChapter: ((List<ChapterItem>, ChapterDownloadAction) -> Unit)?,
    onAddToLibraryClicked: () -> Unit,
    onWebViewClicked: (() -> Unit)?,
    onTrackingClicked: (() -> Unit)?,
    onTagClicked: (String) -> Unit,
    onFilterButtonClicked: () -> Unit,
    onRefresh: () -> Unit,
    onContinueReading: () -> Unit,
    onSearch: (query: String, global: Boolean) -> Unit,

    // For cover dialog
    onCoverClicked: () -> Unit,

    // For top action menu
    onShareClicked: (() -> Unit)?,
    onDownloadActionClicked: ((DownloadAction) -> Unit)?,
    onEditCategoryClicked: (() -> Unit)?,
    onMigrateClicked: (() -> Unit)?,

    // For bottom action menu
    onMultiBookmarkClicked: (List<Chapter>, bookmarked: Boolean) -> Unit,
    onMultiMarkAsReadClicked: (List<Chapter>, markAsRead: Boolean) -> Unit,
    onMarkPreviousAsReadClicked: (Chapter) -> Unit,
    onMultiDeleteClicked: (List<Chapter>) -> Unit,

    // Chapter selection
    onChapterSelected: (ChapterItem, Boolean, Boolean, Boolean) -> Unit,
    onAllChapterSelected: (Boolean) -> Unit,
    onInvertSelection: () -> Unit,
) {
    if (!isTabletUi) {
        MangaScreenSmallImpl(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onChapterClicked = onChapterClicked,
            onDownloadChapter = onDownloadChapter,
            onAddToLibraryClicked = onAddToLibraryClicked,
            onWebViewClicked = onWebViewClicked,
            onTrackingClicked = onTrackingClicked,
            onTagClicked = onTagClicked,
            onFilterClicked = onFilterButtonClicked,
            onRefresh = onRefresh,
            onContinueReading = onContinueReading,
            onSearch = onSearch,
            onCoverClicked = onCoverClicked,
            onShareClicked = onShareClicked,
            onDownloadActionClicked = onDownloadActionClicked,
            onEditCategoryClicked = onEditCategoryClicked,
            onMigrateClicked = onMigrateClicked,
            onMultiBookmarkClicked = onMultiBookmarkClicked,
            onMultiMarkAsReadClicked = onMultiMarkAsReadClicked,
            onMarkPreviousAsReadClicked = onMarkPreviousAsReadClicked,
            onMultiDeleteClicked = onMultiDeleteClicked,
            onChapterSelected = onChapterSelected,
            onAllChapterSelected = onAllChapterSelected,
            onInvertSelection = onInvertSelection,
        )
    } else {
        MangaScreenLargeImpl(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onChapterClicked = onChapterClicked,
            onDownloadChapter = onDownloadChapter,
            onAddToLibraryClicked = onAddToLibraryClicked,
            onWebViewClicked = onWebViewClicked,
            onTrackingClicked = onTrackingClicked,
            onTagClicked = onTagClicked,
            onFilterButtonClicked = onFilterButtonClicked,
            onRefresh = onRefresh,
            onContinueReading = onContinueReading,
            onSearch = onSearch,
            onCoverClicked = onCoverClicked,
            onShareClicked = onShareClicked,
            onDownloadActionClicked = onDownloadActionClicked,
            onEditCategoryClicked = onEditCategoryClicked,
            onMigrateClicked = onMigrateClicked,
            onMultiBookmarkClicked = onMultiBookmarkClicked,
            onMultiMarkAsReadClicked = onMultiMarkAsReadClicked,
            onMarkPreviousAsReadClicked = onMarkPreviousAsReadClicked,
            onMultiDeleteClicked = onMultiDeleteClicked,
            onChapterSelected = onChapterSelected,
            onAllChapterSelected = onAllChapterSelected,
            onInvertSelection = onInvertSelection,
        )
    }
}

@Composable
private fun MangaScreenSmallImpl(
    state: MangaScreenState.Success,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onChapterClicked: (Chapter) -> Unit,
    onDownloadChapter: ((List<ChapterItem>, ChapterDownloadAction) -> Unit)?,
    onAddToLibraryClicked: () -> Unit,
    onWebViewClicked: (() -> Unit)?,
    onTrackingClicked: (() -> Unit)?,
    onTagClicked: (String) -> Unit,
    onFilterClicked: () -> Unit,
    onRefresh: () -> Unit,
    onContinueReading: () -> Unit,
    onSearch: (query: String, global: Boolean) -> Unit,

    // For cover dialog
    onCoverClicked: () -> Unit,

    // For top action menu
    onShareClicked: (() -> Unit)?,
    onDownloadActionClicked: ((DownloadAction) -> Unit)?,
    onEditCategoryClicked: (() -> Unit)?,
    onMigrateClicked: (() -> Unit)?,

    // For bottom action menu
    onMultiBookmarkClicked: (List<Chapter>, bookmarked: Boolean) -> Unit,
    onMultiMarkAsReadClicked: (List<Chapter>, markAsRead: Boolean) -> Unit,
    onMarkPreviousAsReadClicked: (Chapter) -> Unit,
    onMultiDeleteClicked: (List<Chapter>) -> Unit,

    // Chapter selection
    onChapterSelected: (ChapterItem, Boolean, Boolean, Boolean) -> Unit,
    onAllChapterSelected: (Boolean) -> Unit,
    onInvertSelection: () -> Unit,
) {
    val chapterListState = rememberLazyListState()

    val chapters = remember(state) { state.processedChapters.toList() }

    val internalOnBackPressed = {
        if (chapters.any { it.selected }) {
            onAllChapterSelected(false)
        } else {
            onBackClicked()
        }
    }
    BackHandler(onBack = internalOnBackPressed)

    Scaffold(
        modifier = Modifier
            .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        topBar = {
            val firstVisibleItemIndex by remember {
                derivedStateOf { chapterListState.firstVisibleItemIndex }
            }
            val firstVisibleItemScrollOffset by remember {
                derivedStateOf { chapterListState.firstVisibleItemScrollOffset }
            }
            val animatedTitleAlpha by animateFloatAsState(
                if (firstVisibleItemIndex > 0) 1f else 0f,
            )
            val animatedBgAlpha by animateFloatAsState(
                if (firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0) 1f else 0f,
            )
            MangaToolbar(
                title = state.manga.title,
                titleAlphaProvider = { animatedTitleAlpha },
                backgroundAlphaProvider = { animatedBgAlpha },
                hasFilters = state.manga.chaptersFiltered(),
                incognitoMode = state.isIncognitoMode,
                downloadedOnlyMode = state.isDownloadedOnlyMode,
                onBackClicked = internalOnBackPressed,
                onClickFilter = onFilterClicked,
                onClickShare = onShareClicked,
                onClickDownload = onDownloadActionClicked,
                onClickEditCategory = onEditCategoryClicked,
                onClickMigrate = onMigrateClicked,
                actionModeCounter = chapters.count { it.selected },
                onSelectAll = { onAllChapterSelected(true) },
                onInvertSelection = { onInvertSelection() },
            )
        },
        bottomBar = {
            SharedMangaBottomActionMenu(
                selected = chapters.filter { it.selected },
                onMultiBookmarkClicked = onMultiBookmarkClicked,
                onMultiMarkAsReadClicked = onMultiMarkAsReadClicked,
                onMarkPreviousAsReadClicked = onMarkPreviousAsReadClicked,
                onDownloadChapter = onDownloadChapter,
                onMultiDeleteClicked = onMultiDeleteClicked,
                fillFraction = 1f,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = chapters.any { !it.chapter.read } && chapters.none { it.selected },
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        val id = if (chapters.any { it.chapter.read }) {
                            R.string.action_resume
                        } else {
                            R.string.action_start
                        }
                        Text(text = stringResource(id))
                    },
                    icon = { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null) },
                    onClick = onContinueReading,
                    expanded = chapterListState.isScrollingUp() || chapterListState.isScrolledToEnd(),
                    modifier = Modifier
                        .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues()),
                )
            }
        },
    ) { contentPadding ->
        val topPadding = contentPadding.calculateTopPadding()

        SwipeRefresh(
            refreshing = state.isRefreshingData,
            onRefresh = onRefresh,
            enabled = chapters.none { it.selected },
            indicatorPadding = contentPadding,
        ) {
            VerticalFastScroller(
                listState = chapterListState,
                topContentPadding = topPadding,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    state = chapterListState,
                    contentPadding = PaddingValues(
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                ) {
                    item(
                        key = MangaScreenItem.INFO_BOX,
                        contentType = MangaScreenItem.INFO_BOX,
                    ) {
                        MangaInfoBox(
                            isTabletUi = false,
                            appBarPadding = topPadding,
                            title = state.manga.title,
                            author = state.manga.author,
                            artist = state.manga.artist,
                            sourceName = remember { state.source.getNameForMangaInfo() },
                            isStubSource = remember { state.source is SourceManager.StubSource },
                            coverDataProvider = { state.manga },
                            status = state.manga.status,
                            onCoverClick = onCoverClicked,
                            doSearch = onSearch,
                        )
                    }

                    item(
                        key = MangaScreenItem.ACTION_ROW,
                        contentType = MangaScreenItem.ACTION_ROW,
                    ) {
                        MangaActionRow(
                            favorite = state.manga.favorite,
                            trackingCount = state.trackingCount,
                            onAddToLibraryClicked = onAddToLibraryClicked,
                            onWebViewClicked = onWebViewClicked,
                            onTrackingClicked = onTrackingClicked,
                            onEditCategory = onEditCategoryClicked,
                        )
                    }

                    item(
                        key = MangaScreenItem.DESCRIPTION_WITH_TAG,
                        contentType = MangaScreenItem.DESCRIPTION_WITH_TAG,
                    ) {
                        ExpandableMangaDescription(
                            defaultExpandState = state.isFromSource,
                            description = state.manga.description,
                            tagsProvider = { state.manga.genre },
                            onTagClicked = onTagClicked,
                        )
                    }

                    item(
                        key = MangaScreenItem.CHAPTER_HEADER,
                        contentType = MangaScreenItem.CHAPTER_HEADER,
                    ) {
                        ChapterHeader(
                            chapterCount = chapters.size,
                            onClick = onFilterClicked,
                        )
                    }

                    sharedChapterItems(
                        chapters = chapters,
                        onChapterClicked = onChapterClicked,
                        onDownloadChapter = onDownloadChapter,
                        onChapterSelected = onChapterSelected,
                    )
                }
            }
        }
    }
}

@Composable
fun MangaScreenLargeImpl(
    state: MangaScreenState.Success,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onChapterClicked: (Chapter) -> Unit,
    onDownloadChapter: ((List<ChapterItem>, ChapterDownloadAction) -> Unit)?,
    onAddToLibraryClicked: () -> Unit,
    onWebViewClicked: (() -> Unit)?,
    onTrackingClicked: (() -> Unit)?,
    onTagClicked: (String) -> Unit,
    onFilterButtonClicked: () -> Unit,
    onRefresh: () -> Unit,
    onContinueReading: () -> Unit,
    onSearch: (query: String, global: Boolean) -> Unit,

    // For cover dialog
    onCoverClicked: () -> Unit,

    // For top action menu
    onShareClicked: (() -> Unit)?,
    onDownloadActionClicked: ((DownloadAction) -> Unit)?,
    onEditCategoryClicked: (() -> Unit)?,
    onMigrateClicked: (() -> Unit)?,

    // For bottom action menu
    onMultiBookmarkClicked: (List<Chapter>, bookmarked: Boolean) -> Unit,
    onMultiMarkAsReadClicked: (List<Chapter>, markAsRead: Boolean) -> Unit,
    onMarkPreviousAsReadClicked: (Chapter) -> Unit,
    onMultiDeleteClicked: (List<Chapter>) -> Unit,

    // Chapter selection
    onChapterSelected: (ChapterItem, Boolean, Boolean, Boolean) -> Unit,
    onAllChapterSelected: (Boolean) -> Unit,
    onInvertSelection: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current

    val chapters = remember(state) { state.processedChapters.toList() }

    val insetPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal).asPaddingValues()
    val (topBarHeight, onTopBarHeightChanged) = remember { mutableStateOf(0) }
    SwipeRefresh(
        refreshing = state.isRefreshingData,
        onRefresh = onRefresh,
        enabled = chapters.none { it.selected },
        indicatorPadding = PaddingValues(
            start = insetPadding.calculateStartPadding(layoutDirection),
            top = with(density) { topBarHeight.toDp() },
            end = insetPadding.calculateEndPadding(layoutDirection),
        ),
    ) {
        val chapterListState = rememberLazyListState()

        val internalOnBackPressed = {
            if (chapters.any { it.selected }) {
                onAllChapterSelected(false)
            } else {
                onBackClicked()
            }
        }
        BackHandler(onBack = internalOnBackPressed)

        Scaffold(
            modifier = Modifier.padding(insetPadding),
            topBar = {
                MangaToolbar(
                    modifier = Modifier.onSizeChanged { onTopBarHeightChanged(it.height) },
                    title = state.manga.title,
                    titleAlphaProvider = { if (chapters.any { it.selected }) 1f else 0f },
                    backgroundAlphaProvider = { 1f },
                    hasFilters = state.manga.chaptersFiltered(),
                    incognitoMode = state.isIncognitoMode,
                    downloadedOnlyMode = state.isDownloadedOnlyMode,
                    onBackClicked = internalOnBackPressed,
                    onClickFilter = onFilterButtonClicked,
                    onClickShare = onShareClicked,
                    onClickDownload = onDownloadActionClicked,
                    onClickEditCategory = onEditCategoryClicked,
                    onClickMigrate = onMigrateClicked,
                    actionModeCounter = chapters.count { it.selected },
                    onSelectAll = { onAllChapterSelected(true) },
                    onInvertSelection = { onInvertSelection() },
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    SharedMangaBottomActionMenu(
                        selected = chapters.filter { it.selected },
                        onMultiBookmarkClicked = onMultiBookmarkClicked,
                        onMultiMarkAsReadClicked = onMultiMarkAsReadClicked,
                        onMarkPreviousAsReadClicked = onMarkPreviousAsReadClicked,
                        onDownloadChapter = onDownloadChapter,
                        onMultiDeleteClicked = onMultiDeleteClicked,
                        fillFraction = 0.5f,
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = chapters.any { !it.chapter.read } && chapters.none { it.selected },
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    ExtendedFloatingActionButton(
                        text = {
                            val id = if (chapters.any { it.chapter.read }) {
                                R.string.action_resume
                            } else {
                                R.string.action_start
                            }
                            Text(text = stringResource(id))
                        },
                        icon = { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null) },
                        onClick = onContinueReading,
                        expanded = chapterListState.isScrollingUp() || chapterListState.isScrolledToEnd(),
                        modifier = Modifier
                            .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues()),
                    )
                }
            },
        ) { contentPadding ->
            TwoPanelBox(
                startContent = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()),
                    ) {
                        MangaInfoBox(
                            isTabletUi = true,
                            appBarPadding = contentPadding.calculateTopPadding(),
                            title = state.manga.title,
                            author = state.manga.author,
                            artist = state.manga.artist,
                            sourceName = remember { state.source.getNameForMangaInfo() },
                            isStubSource = remember { state.source is SourceManager.StubSource },
                            coverDataProvider = { state.manga },
                            status = state.manga.status,
                            onCoverClick = onCoverClicked,
                            doSearch = onSearch,
                        )
                        MangaActionRow(
                            favorite = state.manga.favorite,
                            trackingCount = state.trackingCount,
                            onAddToLibraryClicked = onAddToLibraryClicked,
                            onWebViewClicked = onWebViewClicked,
                            onTrackingClicked = onTrackingClicked,
                            onEditCategory = onEditCategoryClicked,
                        )
                        ExpandableMangaDescription(
                            defaultExpandState = true,
                            description = state.manga.description,
                            tagsProvider = { state.manga.genre },
                            onTagClicked = onTagClicked,
                        )
                    }
                },
                endContent = {
                    VerticalFastScroller(
                        listState = chapterListState,
                        topContentPadding = contentPadding.calculateTopPadding(),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxHeight(),
                            state = chapterListState,
                            contentPadding = PaddingValues(
                                top = contentPadding.calculateTopPadding(),
                                bottom = contentPadding.calculateBottomPadding(),
                            ),
                        ) {
                            item(
                                key = MangaScreenItem.CHAPTER_HEADER,
                                contentType = MangaScreenItem.CHAPTER_HEADER,
                            ) {
                                ChapterHeader(
                                    chapterCount = chapters.size,
                                    onClick = onFilterButtonClicked,
                                )
                            }

                            sharedChapterItems(
                                chapters = chapters,
                                onChapterClicked = onChapterClicked,
                                onDownloadChapter = onDownloadChapter,
                                onChapterSelected = onChapterSelected,
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun SharedMangaBottomActionMenu(
    selected: List<ChapterItem>,
    modifier: Modifier = Modifier,
    onMultiBookmarkClicked: (List<Chapter>, bookmarked: Boolean) -> Unit,
    onMultiMarkAsReadClicked: (List<Chapter>, markAsRead: Boolean) -> Unit,
    onMarkPreviousAsReadClicked: (Chapter) -> Unit,
    onDownloadChapter: ((List<ChapterItem>, ChapterDownloadAction) -> Unit)?,
    onMultiDeleteClicked: (List<Chapter>) -> Unit,
    fillFraction: Float,
) {
    MangaBottomActionMenu(
        visible = selected.isNotEmpty(),
        modifier = modifier.fillMaxWidth(fillFraction),
        onBookmarkClicked = {
            onMultiBookmarkClicked.invoke(selected.map { it.chapter }, true)
        }.takeIf { selected.any { !it.chapter.bookmark } },
        onRemoveBookmarkClicked = {
            onMultiBookmarkClicked.invoke(selected.map { it.chapter }, false)
        }.takeIf { selected.all { it.chapter.bookmark } },
        onMarkAsReadClicked = {
            onMultiMarkAsReadClicked(selected.map { it.chapter }, true)
        }.takeIf { selected.any { !it.chapter.read } },
        onMarkAsUnreadClicked = {
            onMultiMarkAsReadClicked(selected.map { it.chapter }, false)
        }.takeIf { selected.any { it.chapter.read || it.chapter.lastPageRead > 0L } },
        onMarkPreviousAsReadClicked = {
            onMarkPreviousAsReadClicked(selected[0].chapter)
        }.takeIf { selected.size == 1 },
        onDownloadClicked = {
            onDownloadChapter!!(selected.toList(), ChapterDownloadAction.START)
        }.takeIf {
            onDownloadChapter != null && selected.any { it.downloadState != Download.State.DOWNLOADED }
        },
        onDeleteClicked = {
            onMultiDeleteClicked(selected.map { it.chapter })
        }.takeIf {
            onDownloadChapter != null && selected.any { it.downloadState == Download.State.DOWNLOADED }
        },
    )
}

private fun LazyListScope.sharedChapterItems(
    chapters: List<ChapterItem>,
    onChapterClicked: (Chapter) -> Unit,
    onDownloadChapter: ((List<ChapterItem>, ChapterDownloadAction) -> Unit)?,
    onChapterSelected: (ChapterItem, Boolean, Boolean, Boolean) -> Unit,
) {
    items(
        items = chapters,
        key = { "chapter-${it.chapter.id}" },
        contentType = { MangaScreenItem.CHAPTER },
    ) { chapterItem ->
        val haptic = LocalHapticFeedback.current
        MangaChapterListItem(
            title = chapterItem.chapterTitleString,
            date = chapterItem.dateUploadString,
            readProgress = chapterItem.readProgressString,
            scanlator = chapterItem.chapter.scanlator.takeIf { !it.isNullOrBlank() },
            read = chapterItem.chapter.read,
            bookmark = chapterItem.chapter.bookmark,
            selected = chapterItem.selected,
            downloadStateProvider = { chapterItem.downloadState },
            downloadProgressProvider = { chapterItem.downloadProgress },
            onLongClick = {
                onChapterSelected(chapterItem, !chapterItem.selected, true, true)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onClick = {
                onChapterItemClick(
                    chapterItem = chapterItem,
                    chapters = chapters,
                    onToggleSelection = { onChapterSelected(chapterItem, !chapterItem.selected, true, false) },
                    onChapterClicked = onChapterClicked,
                )
            },
            onDownloadClick = if (onDownloadChapter != null) {
                { onDownloadChapter(listOf(chapterItem), it) }
            } else {
                null
            },
        )
    }
}

private fun onChapterItemClick(
    chapterItem: ChapterItem,
    chapters: List<ChapterItem>,
    onToggleSelection: (Boolean) -> Unit,
    onChapterClicked: (Chapter) -> Unit,
) {
    when {
        chapterItem.selected -> onToggleSelection(false)
        chapters.any { it.selected } -> onToggleSelection(true)
        else -> onChapterClicked(chapterItem.chapter)
    }
}
