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
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_FILTER));
        if(filters.contains(filter))
            throw new FilterException(AppProperties.getInstance().getMessage(ErrorCodes.FILTER_IN_COLLECTION));
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
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_FILTER));
        if(!filters.contains(filter))
            throw new FilterException(AppProperties.getInstance().getMessage(ErrorCodes.FILTER_NOT_IN_COLLECTION));
        filters.remove(filter);
    }

    
    /**
     * calls filter method on all its Filter collection
     * @throws IllegalStateException if the filter list is empty
    */
    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard) {
        if(filters.isEmpty())
            throw new IllegalStateException(AppProperties.getInstance().getMessage(ErrorCodes.NO_FILTER_IN_COLLECTION));
        
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
            if (other.filters != null)
                return false;
        } else if (!filters.equals(other.filters))
            return false;
        return true;
    }


    

}