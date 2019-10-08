import { Controller, Post, Body, HttpStatus, HttpCode } from '@nestjs/common';
import { DtoLoginRequest } from './dto.login.request';
import { DtoLoginResponse } from './dto.login.response';
import { ApiResponse, ApiUseTags } from '@nestjs/swagger';

@ApiUseTags('auth')
@Controller('auth')
export class AuthController {
    @Post('login')
    @HttpCode(HttpStatus.OK)
    @ApiResponse({ status: 200, type: DtoLoginResponse, description: 'returns auth token' })
    async login(@Body() dto: DtoLoginRequest): Promise<DtoLoginResponse> {
        const response = new DtoLoginResponse();
        response.user = { email: dto.email, firstName: 'foo', lastName: 'bar' };
        response.token = 'success-token';

        return response;
    }
}
