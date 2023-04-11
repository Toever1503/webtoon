import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'
import IUserType from '../../../pages/user/types/IUserType'

export interface UserState {
  data: IUserType[]
}

const initialState: UserState = {
  data: [],
}

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    addNewUser: (state, action: PayloadAction<{
      data: IUserType,
      pageSize: number | undefined,
    }>) => {
      if (state.data.length === action.payload.pageSize) {
        state.data.pop();
      }
      state.data = [action.payload.data, ...state.data]
    },
    editUser: (state, action: PayloadAction<IUserType>) => {
      state.data = state.data.map((item: IUserType) => {
        if (item.id === action.payload.id) {
          return action.payload;
        }
        return item;
      });
    },
    deleteUser: (state, action: PayloadAction<number>) => {
      state.data = state.data.filter((item: IUserType) => item.id !== action.payload);
    },
    setUserData: (state, action: PayloadAction<IUserType[]>) => {
      state.data = action.payload;
    },
  },
})

// Action creators are generated for each case reducer function
export const { addNewUser, editUser, deleteUser, setUserData } = userSlice.actions

export default userSlice.reducer