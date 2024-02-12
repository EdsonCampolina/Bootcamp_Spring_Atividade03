package com.devsuperior.bds04.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;

	@Autowired
	private CityService cityService;

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> list = repository.findAll(pageable);
		return list.map(x -> new EventDTO(x));
	}

//	@Transactional(readOnly = true)
//	public CategoryDTO findById(Long id) {
//		Optional<Category> obj = repository.findById(id);
//		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
//		return new CategoryDTO(entity);
//	}

	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(cityService.findById(dto.getCityId()));
		entity = repository.save(entity);
		return new EventDTO(entity);
	}

//	@Transactional
//	public CategoryDTO update(Long id, CategoryDTO dto) {
//		try {
//			Category entity = repository.getOne(id);
//			entity.setName(dto.getName());
//			entity = repository.save(entity);
//			return new CategoryDTO(entity);
//		}
//		catch (EntityNotFoundException e) {
//			throw new ResourceNotFoundException("Id not found " + id);
//		}		
//	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
