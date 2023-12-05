package ie.ncirl.esta.service.impl;

import ie.ncirl.esta.dto.CarerDto;
import ie.ncirl.esta.dto.ChildDto;
import ie.ncirl.esta.dto.TherapistDto;
import ie.ncirl.esta.dto.mapper.CarerMapper;
import ie.ncirl.esta.dto.mapper.ChildMapper;
import ie.ncirl.esta.dto.mapper.TherapistMapper;
import ie.ncirl.esta.repository.CarerRepository;
import ie.ncirl.esta.repository.ChildRepository;
import ie.ncirl.esta.repository.TherapistRepository;
import ie.ncirl.esta.service.TherapistService;
import ie.ncirl.esta.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service("therapistServiceImpl")
@RequiredArgsConstructor
public class TherapistServiceImpl implements TherapistService {

    private final TherapistRepository therapistRepository;
    private final CarerRepository carerRepository;
    private final ChildRepository childRepository;
    private final VerificationService verificationService;

    @Override
    public List<TherapistDto> findAll() {
        return therapistRepository.findAll().stream().map(TherapistMapper::toDto).toList();
    }

    @Override
    public Optional<TherapistDto> findById(Long id) {
        var user = therapistRepository.findById(id);

        if (user.isPresent()) {
            return Optional.of(TherapistMapper.toDto(user.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TherapistDto> findByEmail(String email) {
        var user = therapistRepository.findTherapistByEmail(email);

        if (user.isPresent()) {
            return Optional.of(TherapistMapper.toDto(user.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public CarerDto createCarer(Long therapistId, CarerDto carerDto) {
        var therapist = therapistRepository.findById(therapistId).get();
        var carer = CarerMapper.toEntity(carerDto);

        carer.setUserName(carer.getEmail());
        carer.setCreatedOn(Instant.now());
        carer.setConfirmed(false);
        carer.setTherapist(therapist);
        carer = carerRepository.save(carer);

        verificationService.sendVerificationRequest(carer.getId(), carer.getEmail());

        return CarerMapper.toDto(carer);
    }

    @Override
    public ChildDto createChild(Long carerId, ChildDto childDto) {
        var child = ChildMapper.toEntity(childDto);
        var carer = carerRepository.findById(carerId).get();

        child.setCarer(carer);
        child.setCreatedOn(Instant.now());
        child.setConfirmed(false);
        child = childRepository.save(child);

        verificationService.sendVerificationRequest(child.getId(), carer.getEmail());

        return ChildMapper.toDto(child);
    }
}