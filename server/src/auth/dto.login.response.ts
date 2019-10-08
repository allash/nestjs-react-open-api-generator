import { DtoUserResponse } from './dto.user.response';
import { ApiResponseModelProperty } from '@nestjs/swagger';

export class DtoLoginResponse {
    @ApiResponseModelProperty()
    public token: string;
    @ApiResponseModelProperty()
    public user: DtoUserResponse;
}
