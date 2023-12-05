package ie.ncirl.esta.service.impl;

import ie.ncirl.esta.dto.ChildDto;
import ie.ncirl.esta.dto.mapper.ChildMapper;
import ie.ncirl.esta.repository.CarerRepository;
import ie.ncirl.esta.repository.ChildRepository;
import ie.ncirl.esta.service.CarerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CarerServiceImpl implements CarerService {

    private final CarerRepository carerRepository;
    private final ChildRepository childRepository;

    @Override
    public ChildDto createChild(Long carerId, ChildDto childDto) {
        var child = ChildMapper.toEntity(childDto);
        var carer = carerRepository.findById(carerId).get();

        child.setCarer(carer);
        child.setCreatedOn(Instant.now());
        child.setConfirmed(true);
        child = childRepository.save(child);

        return ChildMapper.toDto(child);
    }
}
