import { createSlice } from '@reduxjs/toolkit'

export interface GenreModel {
    key: string | number;
    id: string | number;
    name: string;
    slug: string;
    stt: string | number;
}

export interface GenreState {
    data: Array<GenreModel>,
    totalElements: number,
    size: number,
}

const initialState: GenreState = {
    data: [
        {
            key: 1,
            id: 1,
            stt: 1,
            name: 'John Brown',
            slug: 'New York No. 1 Lake Park',
        },
    ],
    totalElements: 1,
    size: 10,
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

            if (state.data.length === state.size)
                state.data.pop();
            state.data.unshift(payload);

            state.totalElements = state.totalElements + 1;
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
        deleteGenreById: (state,{payload}) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
            console.log('deleteTagById', state.data);
            state.totalElements = state.totalElements - 1;
        },
        setGenreData: (state, { payload }) => {
            state.data = payload.data;
            state.totalElements = payload.totalElements;
        }

    },
})

// Action creators are generated for each case reducer function
export const { addGenre, updateGenre, deleteGenreById, setGenreData } = genreSlice.actions

export default genreSlice.reducer