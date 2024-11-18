import { createSlice } from "@reduxjs/toolkit";
let initialState = {
    userValue: {},
};

const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        getUser(state, action) {
            state.userValue = action.payload.data;
        },
    },
});

export const userActions = userSlice.actions;
export default userSlice.reducer;
