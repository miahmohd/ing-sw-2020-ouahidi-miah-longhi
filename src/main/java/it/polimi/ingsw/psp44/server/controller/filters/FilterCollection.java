package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.FilterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter Collection extends Filter.
 * Has simple collection methods (add and remove)
 * Architecture based on the composite pattern (https://en.wikipedia.org/wiki/Composite_pattern)
 */
public class FilterCollection extends Filter {

    private final List<Filter> filters;

    public FilterCollection() {
        this.filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the list of filters in the collection
     *
     * @param filter is the filter to be added to the collection
     * @throws IllegalArgumentException if Filter is null
     * @throws FilterException          if filter is already in the list
     */
    public void add(Filter filter) {
        if (filter == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_FILTER));
        if (filters.contains(filter))
            throw new FilterException(R.getAppProperties().getProperty(ErrorCodes.FILTER_IN_COLLECTION));
        filters.add(filter);
    }

    /**
     * check if a filter is already in the collection
     * @param filter
     * @return
     */
    public boolean contains(Filter filter){
        return filters.contains(filter);
    }

    /**
     * Removes a filter form the list of filters
     *
     * @param filter is the filter to be removed from the collection
     * @throws IllegalArgumentException if Filter is null
     * @throws FilterException          if filter is not in the list
     */
    public void remove(Filter filter) {
        if (filter == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_FILTER));
        if (!filters.contains(filter))
            throw new FilterException(R.getAppProperties().getProperty(ErrorCodes.FILTER_NOT_IN_COLLECTION));
        filters.remove(filter);
    }

    /**
     * Update the sate of the selected filters
     * @param lastAction
     */
    public void update(Filter filter,Action lastAction){
        for (Filter f: filters){
            if(filter.equals(f))
                f.update(lastAction);
        }

    }

    /**
     * return the filters of the collection
     *
     * @return a list of the filters in the collection
     */
    public List<Filter> getFilters() {
        return filters;
    }


    /**
     * calls filter method on all its Filter collection
     *
     * @throws IllegalStateException if the filter list is empty
     */
    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard) {
        if (filters.isEmpty())
            throw new IllegalStateException(R.getAppProperties().getProperty(ErrorCodes.NO_FILTER_IN_COLLECTION));

        for (Filter filter : filters) {
            filter.filter(startingPosition, positionsToFilter, gameBoard);
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((filters == null) ? 0 : filters.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FilterCollection other = (FilterCollection) obj;
        if (filters == null) {
            return other.filters == null;
        } else return filters.equals(other.filters);
    }


}