package edu.pro.spbbase.model;
/*
  @author   george
  @project   spb-base
  @class  Item
  @version  1.0.0 
  @since 11.02.24 - 12.15
*/

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Item {
    @Id
    private String id; // UUID
    private String name;
    private String code;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
