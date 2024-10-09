package com.eric.pageing3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eric.base.data.Product
import com.eric.base.data.ProductDao
import kotlinx.coroutines.flow.Flow

// ProductRepository.kt
class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { productDao.getAllProducts() }
        ).flow
    }
}
