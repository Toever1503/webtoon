import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'


export type NotificationType = 'success' | 'info' | 'warning' | 'error';

export interface NotificationState {
  isShow?: boolean,
  type: NotificationType,
  message: string
}

const initialState: NotificationState = {
  isShow: false,
  type: 'warning',
  message: ''
}

export const notificationSlice = createSlice({
  name: 'counter',
  initialState,
  reducers: {
    showNofification: (state,  { payload } : PayloadAction<NotificationState>) => {
      // Redux Toolkit allows us to write "mutating" logic in reducers. It
      // doesn't actually mutate the state because it uses the Immer library,
      // which detects changes to a "draft state" and produces a brand new
      // immutable state based off those changes

      console.log('showNofification', payload)
      state.isShow = true
      state.message = payload.message
      state.type = payload.type
    },
    hideNofification: (state) => {
      state.isShow = false
      state.message = ''
    },
  },
})

// Action creators are generated for each case reducer function
export const { showNofification, hideNofification } = notificationSlice.actions

export default notificationSlice.reducer