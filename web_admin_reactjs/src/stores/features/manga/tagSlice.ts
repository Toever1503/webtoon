import { createSlice } from '@reduxjs/toolkit'

export interface TagModel {
    key: string | number;
    name: string;
    slug: string;
    stt: string | number;
}

export interface TagState {
    data: Array<TagModel>
}

const initialState: TagState = {
    data: [
        {
            key: 1,
            stt: 1,
            name: 'John Brown',
            slug: 'New York No. 1 Lake Park',
        },
    ]
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

            state.data.push(payload);
        },
        updateTag: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.key === payload.key) {
                    return payload;
                }
                return item;
            });
        },
        deleteTag: (state) => { }
    },
})

// Action creators are generated for each case reducer function
export const { addTag, updateTag, deleteTag } = tagSlice.actions

export default tagSlice.reducer