import { createSlice } from '@reduxjs/toolkit'

export interface GenreModel {
    key: string | number;
    id: string | number;
    name: string;
    slug: string;
    index: string | number;
}

export interface GenreState {
    data: Array<GenreModel>,
}

const initialState: GenreState = {
    data: [
        {
            key: 1,
            id: 1,
            index: 1,
            name: 'John Brown',
            slug: 'New York No. 1 Lake Park',
        },
    ],
}

export const genreSlice = createSlice({
    name: 'genre',
    initialState,
    reducers: {
        addGenre: (state, { payload }) => {
            // Redux Toolkit allows us to write "mutating" logic in reducers. It
            // doesn't actually mutate the state because it uses the Immer library,
            // which detects changes to a "draft state" and produces a brand new
            // immutable state based off those changes

            // state.data.push(payload);
            if (!payload.key)
                payload.key = payload.id;
            state.data = [payload, ...state.data]

            console.log('addTag', payload)
        },
        updateGenre: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.id === payload.id) {
                    return payload;
                }
                return item;
            });
        },
        deleteGenreById: (state, { payload }) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
            console.log('deleteTagById', state.data);
        },
        setGenreData: (state, { payload }) => {
            state.data = payload.data;
        }

    },
})

// Action creators are generated for each case reducer function
export const { addGenre, updateGenre, deleteGenreById, setGenreData } = genreSlice.actions

export default genreSlice.reducer