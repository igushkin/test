package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.ChildDto;

public interface CarerService {
    ChildDto createChild(Long carerId, ChildDto childDto);
}
