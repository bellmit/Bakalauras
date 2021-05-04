import thunk from 'redux-thunk';
import nock from 'nock';
import configureMockStore from 'redux-mock-store';
import * as TradeActions from '../TradeAction';
import * as ActionType from '../ActionType';



describe('TradeAction.test.js', () => {

    describe('getTradesResponseAction Creator', () => {
        it(`should create action ${ActionType.GET_COURSES_RESPONSE}`, () => {
            const courses = [{ title: 'Learn reactjs redux' }];
            const expectedAction = {
                type: ActionType.GET_COURSES_RESPONSE,
                courses: courses
            };

            const actualAction = TradeActions.getTradesResponse(courses);

            expect(actualAction).toEqual(expectedAction);
        });
    });


    describe('addNewTradeResponseAction Creator', () => {
        it(`should create action ${ActionType.ADD_NEW_COURSE_RESPONSE}`, () => {
            const course = { title: 'Learn reactjs redux' };
            const expectedAction = {
                type: ActionType.ADD_NEW_COURSE_RESPONSE
            };

            const actualAction = TradeActions.addNewTradeResponse(course);

            expect(actualAction).toEqual(expectedAction);
        });
    });


    describe('updateExistingTradeResponseAction Creator', () => {
        it(`should create action ${ActionType.UPDATE_EXISTING_COURSE_RESPONSE}`, () => {
            const course = { title: 'Learn reactjs redux' };
            const expectedAction = {
                type: ActionType.UPDATE_EXISTING_COURSE_RESPONSE
            };

            const actualAction = TradeActions.updateExistingTradeResponse(course);

            expect(actualAction).toEqual(expectedAction);
        });
    });


    describe('getTradeResponseAction Creator', () => {
        it(`should create action ${ActionType.GET_COURSE_RESPONSE}`, () => {
            const course = { title: 'Learn reactjs redux' };
            const expectedAction = {
                type: ActionType.GET_COURSE_RESPONSE,
                course: course
            };

            const actualAction = TradeActions.getTradeResponse(course);

            expect(actualAction).toEqual(expectedAction);
        });
    });



    describe('deleteTradeResponseAction Creator', () => {
        it(`should create action ${ActionType.DELETE_COURSE_RESPONSE}`, () => {
            const expectedAction = {
                type: ActionType.DELETE_COURSE_RESPONSE
            };

            const actualAction = TradeActions.deleteTradeResponse();

            expect(actualAction).toEqual(expectedAction);
        });
    });



    const thunkMiddleware = [thunk];
    const mockStore = configureMockStore(thunkMiddleware);


    describe('getTradesAction Thunk', () => {
        afterEach(() => {
            nock.cleanAll();
        });

        it('should get all courses', (done) => {
            const expectedActions = [
                { type: ActionType.API_CALL_BEGIN },
                {
                    type: ActionType.GET_COURSES_RESPONSE,
                    body: {
                        courses: [
                            { id: 1, title: 'Java Clean Code' }
                        ]
                    }
                }
            ];

            const store = mockStore({ courses: [] }, expectedActions, done);

            store.dispatch(TradeActions.getTradesAction())
                .then(() => {
                    const actions = store.getActions();

                    expect(actions[0].type).toEqual(ActionType.API_CALL_BEGIN);
                    expect(actions[1].type).toEqual(ActionType.GET_COURSES_RESPONSE);
                    done();
                });
        });

    });


    describe('saveTradeAction Thunk', () => {
        afterEach(() => {
            nock.cleanAll();
        });

        it('should update existing course', (done) => {
            const expectedActions = [
                { type: ActionType.API_CALL_BEGIN },
                { type: ActionType.UPDATE_EXISTING_COURSE_RESPONSE}
            ];

            const store = mockStore({ course: [] }, expectedActions, done);
            const course = { id: 1, title: 'Learn reactjs redux' };
            store.dispatch(TradeActions.saveTradeAction(course))
                .then(() => {
                    const actions = store.getActions();

                    expect(actions[0].type).toEqual(ActionType.API_CALL_BEGIN);
                    expect(actions[1].type).toEqual(ActionType.UPDATE_EXISTING_COURSE_RESPONSE);
                    done();
                });
        });


        it('should add a new course', (done) => {
            const expectedActions = [
                { type: ActionType.API_CALL_BEGIN },
                { type: ActionType.ADD_NEW_COURSE_RESPONSE}
            ];

            const store = mockStore({ course: [] }, expectedActions, done);
            const course = { title: 'Learn reactjs redux' };
            store.dispatch(TradeActions.saveTradeAction(course))
                .then(() => {
                    const actions = store.getActions();

                    expect(actions[0].type).toEqual(ActionType.API_CALL_BEGIN);
                    expect(actions[1].type).toEqual(ActionType.ADD_NEW_COURSE_RESPONSE);
                    done();
                });
        });

    });



    describe('getTradeAction Thunk', () => {
        afterEach(() => {
            nock.cleanAll();
        });

        it('should get a specific courses', (done) => {
            const findThisTrade = { id: 1, title: 'Java Clean Code' };

            const expectedActions = [
                { type: ActionType.API_CALL_BEGIN },
                {
                    type: ActionType.GET_COURSE_RESPONSE,
                    body: {
                        course: findThisTrade
                    }
                }
            ];

            const store = mockStore({ course: {} }, expectedActions, done);
            store.dispatch(TradeActions.getTradeAction(1))
                .then(() => {
                    const actions = store.getActions();

                    expect(actions[0].type).toEqual(ActionType.API_CALL_BEGIN);
                    expect(actions[1].type).toEqual(ActionType.GET_COURSE_RESPONSE);
                    done();
                });
        });
    });    



    describe('deleteTradeAction Thunk', () => {
        afterEach(() => {
            nock.cleanAll();
        });

        it('should delete a specific course', (done) => {
            const expectedActions = [
                { type: ActionType.API_CALL_BEGIN },
                {
                    type: ActionType.DELETE_COURSE_RESPONSE
                }
            ];

            const store = mockStore({ course: {} }, expectedActions, done);
            store.dispatch(TradeActions.deleteTradeAction(1))
                .then(() => {
                    const actions = store.getActions();

                    expect(actions[0].type).toEqual(ActionType.API_CALL_BEGIN);
                    expect(actions[1].type).toEqual(ActionType.DELETE_COURSE_RESPONSE);
                    done();
                });
        });
    });    



});


