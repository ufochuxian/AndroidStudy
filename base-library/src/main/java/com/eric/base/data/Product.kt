package com.eric.base.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double
)

// ProductDao.kt
@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): PagingSource<Int, Product>  // 正确处理分页数据

    // 也可以使用 Flow 来处理异步数据流
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProductsAsFlow(): Flow<List<Product>>

    // 插入单个实体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    // 插入多个实体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
}

