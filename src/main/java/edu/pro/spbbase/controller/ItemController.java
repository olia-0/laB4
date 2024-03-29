package edu.pro.spbbase.controller;
/*
  @author   george
  @project   spb-base
  @class  ItemController
  @version  1.0.0 
  @since 11.02.24 - 12.40
*/

import edu.pro.spbbase.model.Item;
import edu.pro.spbbase.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping("")
    public List<Item> fetchAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Item fetchById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Item insert(@RequestBody Item item) {
        return service.create(item);
    }

    @DeleteMapping("/{id}")
    public void eraseById(@PathVariable String id) {
         service.delete(id);
    }


}
