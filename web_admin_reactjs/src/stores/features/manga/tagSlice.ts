import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'

export interface TagModel {
    key: string | number;
    id: string | number;
    tagName: string;
    slug: string;
    index: string | number;
}

export interface TagState {
    data: Array<TagModel>,
    totalElements: number,
    size: number,
}

const initialState = (): TagState => {
    return {
        data: [
            {
                id: 1,
                key: 1,
                index: 1,
                tagName: 'John Brown',
                slug: 'New York No. 1 Lake Park',
            },
        ],
        totalElements: 1,
        size: 10,
    }
}




export const tagSlice = createSlice({
    name: 'tag',
    initialState,
    reducers: {
        addTag: (state, { payload }) => {
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
        },
        updateTag: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.key === payload.key) {
                    return payload;
                }
                return item;
            });
        },
        deleteTagById: (state, { payload }) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
            console.log('deleteTagById', state.data);
            state.totalElements = state.totalElements - 1;
        },
        setTagData: (state, { payload }) => {
            state.data = payload.data;
            state.totalElements = payload.totalElements;
        }
    },
})

// Action creators are generated for each case reducer function
export const { addTag, updateTag, deleteTagById, setTagData } = tagSlice.actions

export default tagSlice.reducer