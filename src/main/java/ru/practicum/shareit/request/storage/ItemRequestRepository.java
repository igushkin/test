package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Integer id);

    List<ItemRequest> findAllByRequestorIdIsNot(Integer id);

    Page<ItemRequest> findAllByRequestorIdIsNot(Integer id, Pageable pageable);


    Optional<ItemRequest> findById(Integer id);
}