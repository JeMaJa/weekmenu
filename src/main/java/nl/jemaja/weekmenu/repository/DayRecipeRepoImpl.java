package nl.jemaja.weekmenu.repository;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class DayRecipeRepoImpl implements DayRecipeRepository {

	@Override
	public List<DayRecipe> findByDateBetweenOrderByDateAsc(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<DayRecipe> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<DayRecipe> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends DayRecipe> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends DayRecipe> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<DayRecipe> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<DayRecipe> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<DayRecipe> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(DayRecipe entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends DayRecipe> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DayRecipe> findByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DayRecipe> findByDateBetween(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DayRecipe> findByRecipe(Recipe recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date findMaxDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<DayRecipe> findByRecipe(Recipe recipe, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<DayRecipe> findByDateBetween(Date startDate, Date endDate, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date findPrevLastEaten(Recipe recipe, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date findNextEaten(Recipe recipe, Date currentdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countEaten(Recipe recipe, Date currentdate) {
		// TODO Auto-generated method stub
		return 0;
	}

}
