import constants from "../utils/constants";
import { Favorite } from "../utils/types/Favorite";
import { Action } from "../utils/types/Action";

export type FavoriteReducerState = {
  listOfFavorites: Favorite[];
  favoriteIdOrder: Favorite["id"][];
};

const INITIAL_STATE = {
  listOfFavorites: [],
  favoriteIdOrder: []
};

export default (
  state: FavoriteReducerState = INITIAL_STATE,
  action: Action
): FavoriteReducerState => {
  switch (action.type) {
    case constants.SET_FAVORITE_LIST:
      return {
        listOfFavorites: action.payload,
        favoriteIdOrder: action.payload.map((fav: Favorite) => fav.id)
      };

    case constants.SET_FAVORITE_ID_ORDER:
      return { ...state, favoriteIdOrder: action.payload };

    case constants.ADD_FAVORITE_TO_LIST:
      return {
        ...state,
        listOfFavorites: [action.payload, ...state.listOfFavorites],
        favoriteIdOrder: [action.payload.id, ...state.favoriteIdOrder]
      };

    case constants.REMOVE_FAVORITE_FROM_LIST:
      return {
        ...state,
        listOfFavorites: state.listOfFavorites.filter(
          fav => fav.id !== action.payload
        ),
        favoriteIdOrder: state.favoriteIdOrder.filter(
          id => id !== action.payload
        )
      };
    case constants.DROP_STATE_ON_UNAUTH:
      return { ...INITIAL_STATE };
    default:
      return state;
  }
};
