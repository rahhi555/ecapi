package com.example.ecapi.repository.product;

import com.example.ecapi.model.DTOProduct;
import com.example.ecapi.model.FormProduct;
import com.github.onozaty.mybatis.pg.type.list.StringListTypeHandler;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductRepository {
    @Select("""
            INSERT INTO
                products(vendor_id, name, description, thumbnail_url, carousel_urls, price, stock)
            VALUES
                (#{vendorId}, #{name}, #{description}, #{thumbnailUrl}, #{carouselUrls, typeHandler=stringListTypeHandler}, #{price}, #{stock})
            RETURNING *;
            """)
    @Result(column = "carousel_urls", property = "carouselUrls", typeHandler = StringListTypeHandler.class)
    DTOProduct insert(FormProduct product);

    @Select("SELECT * FROM products WHERE id = #{id}")
    @Result(column = "carousel_urls", property = "carouselUrls", typeHandler = StringListTypeHandler.class)
    Optional<DTOProduct> select(Integer id);

    @Select("""
            UPDATE
                products
            SET
                thumbnail_url = #{thumbnailUrl}
            WHERE
                id = #{id}
            RETURNING *;
            """)
    @Result(column = "carousel_urls", property = "carouselUrls", typeHandler = StringListTypeHandler.class)
    DTOProduct updateThumbnail(Integer id, String thumbnailUrl);

    @Select("SELECT * FROM products ORDER BY created_at DESC")
    @Result(column = "carousel_urls", property = "carouselUrls", typeHandler = StringListTypeHandler.class)
    List<DTOProduct> selectList();
}
