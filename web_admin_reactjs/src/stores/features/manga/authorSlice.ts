import { createSlice } from '@reduxjs/toolkit'

export interface AuthorModel {
    key: string | number;
    name: string;
    slug: string;
    stt: string | number;
}

export interface AuthorState {
    data: Array<AuthorModel>
}

const initialState: AuthorState = {
    data: [
        {
            key: 1,
            stt: 1,
            name: 'John Brown',
            slug: 'New York No. 1 Lake Park',
        },
    ]
}

export const authorSlice = createSlice({
    name: 'author',
    initialState,
    reducers: {
        addAuthor: (state, { payload }) => {
            // Redux Toolkit allows us to write "mutating" logic in reducers. It
            // doesn't actually mutate the state because it uses the Immer library,
            // which detects changes to a "draft state" and produces a brand new
            // immutable state based off those changes

            state.data.push(payload);
        },
        updateAuthor: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.key === payload.key) {
                    return payload;
                }
                return item;
            });
        },
        deleteAuthor: (state) => { }

    },
})

// Action creators are generated for each case reducer function
export const { addAuthor, updateAuthor, deleteAuthor } = authorSlice.actions

export default authorSlice.reducer