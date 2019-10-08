import { AuthApi, DtoLoginRequest } from '../network';

export class ApiService { 
    private readonly authApi: AuthApi;

    constructor() {
        this.authApi = new AuthApi(undefined, 'http://localhost:3000');
     }

    public async login(dto: DtoLoginRequest) { 
        return this.authApi.authLoginPost(dto);
    }
}