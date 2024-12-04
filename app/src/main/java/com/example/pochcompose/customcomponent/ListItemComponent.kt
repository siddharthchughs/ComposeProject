//package com.example.pochcompose.customcomponent
//
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.paging.compose.LazyPagingItems
//import com.example.pochcompose.home.HeroState
//import com.example.pochcompose.home.HroItem
//import timber.log.Timber
//
//@Composable
//fun HeroLists(
//    heroes: LazyPagingItems<HeroState>
//)
//{
//    LazyColumn (modifier = Modifier.fillMaxWidth()){
//        items(count = heroes.itemCount){4
//            val item = heroes[it]
//            HroItem(item as HeroState)
//            Timber.i("items :: $item")
//        }
//    }
//}