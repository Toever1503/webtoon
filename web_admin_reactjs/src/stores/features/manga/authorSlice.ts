import { createSlice } from '@reduxjs/toolkit'

export interface AuthorModel {
    key: string | number;
    id: string | number;
    name: string;
    slug: string;
    index: string | number;
}

export interface AuthorState {
    data: Array<AuthorModel>,
    totalElements: number,
    size: number,
}

const initialState: AuthorState = {
    data: [
    ],
    totalElements: 1,
    size: 10,
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
            if (!payload.key)
                payload.key = payload.id;

            if (state.data.length === state.size)
                state.data.pop();
            state.data.unshift(payload);

            state.totalElements = state.totalElements + 1;
            console.log('addTag', payload)
            // state.data.push(payload);
        },
        updateAuthor: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.id === payload.id) {
                    return payload;
                }
                return item;
            });
        },
        deleteAuthorById: (state,{payload}) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
            console.log('deleteTagById', state.data);
            state.totalElements = state.totalElements - 1;
        },
        setAuthorData: (state, { payload }) => {
            state.data = payload.data;
            state.totalElements = payload.totalElements;
        }

    },
})

// Action creators are generated for each case reducer function
export const { addAuthor, updateAuthor, deleteAuthorById, setAuthorData } = authorSlice.actions

export default authorSlice.reducer