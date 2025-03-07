package com.eric.pageing3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eric.base.data.Product
import com.eric.base.data.ProductDao
import kotlinx.coroutines.flow.Flow

// ProductRepository.kt
class ProductRepository(private val productDao: ProductDao) {

    fun getPageingProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,  // ✅ 每页 20 个
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(productDao) } // ✅ 使用分页数据源
        ).flow
    }
}
