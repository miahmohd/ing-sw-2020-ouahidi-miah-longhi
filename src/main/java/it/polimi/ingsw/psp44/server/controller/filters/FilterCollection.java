package it.polimi.ingsw.psp44.server.controller.filters;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.FilterException;

/**
 * Filter Collection extends Filter.
 * Has simple collection methods (add and remove)
 * Architecture based on the composite pattern (https://en.wikipedia.org/wiki/Composite_pattern)
*/
public class FilterCollection extends Filter {

    private List<Filter> filters;

    public FilterCollection(){
        this.filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the list of filters in the collection
     * @param filter is the filter to be added to the collection
     * @throws IllegalArgumentException if Filter is null
     * @throws FilterException if filter is already in the list
     */
    public void add(Filter filter){
        if(filter == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getProperty(ErrorCodes.NULL_FILTER));
        if(filters.contains(filter))
            throw new FilterException(AppProperties.getInstance().getProperty(ErrorCodes.FILTER_IN_COLLECTION));
        filters.add(filter);
    }


    /**
     * Removes a filter form the list of filters
     * @param filter is the filter to be removed from the collection
     * @throws IllegalArgumentException if Filter is null
     * @throws FilterException if filter is not in the list
     */
    public void remove(Filter filter){
        if(filter == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getProperty(ErrorCodes.NULL_FILTER));
        if(!filters.contains(filter))
            throw new FilterException(AppProperties.getInstance().getProperty(ErrorCodes.FILTER_NOT_IN_COLLECTION));
        filters.remove(filter);
    }

    /**
     * Removes all the filters from the list
     */
    public void empty(){
        filters.removeIf(filter -> !filter.isExternal());
    }

    /**
     * return the filters of the collection
     * @return a list of the filters in the collection
     */
    public List<Filter> getFilters(){
        return filters;
    }

    
    /**
     * calls filter method on all its Filter collection
     * @throws IllegalStateException if the filter list is empty
    */
    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard, boolean external) {
        if(filters.isEmpty())
            throw new IllegalStateException(AppProperties.getInstance().getProperty(ErrorCodes.NO_FILTER_IN_COLLECTION));
        
        for (Filter filter : filters) {
            filter.filter(startingPosition, positionsToFilter, gameBoard, false);
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
            if (other.filters != null)
                return false;
        } else if (!filters.equals(other.filters))
            return false;
        return true;
    }


    

}