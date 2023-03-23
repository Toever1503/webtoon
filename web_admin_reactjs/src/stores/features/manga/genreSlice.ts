import { createSlice } from '@reduxjs/toolkit'

export interface GenreModel {
    key: string | number;
    name: string;
    slug: string;
    stt: string | number;
}

export interface GenreState {
    data: Array<GenreModel>
}

const initialState: GenreState = {
    data: [
        {
            key: 1,
            stt: 1,
            name: 'John Brown',
            slug: 'New York No. 1 Lake Park',
        },
    ]
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

            state.data.push(payload);
        },
        updateGenre: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.key === payload.key) {
                    return payload;
                }
                return item;
            });
        },
        deleteGenre: (state) => { }

    },
})

// Action creators are generated for each case reducer function
export const { addGenre, updateGenre, deleteGenre } = genreSlice.actions

export default genreSlice.reducer