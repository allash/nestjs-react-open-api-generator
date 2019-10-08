import { ApiModelProperty } from '@nestjs/swagger';

export class DtoLoginRequest {
    @ApiModelProperty({required: true})
    public email: string;
    @ApiModelProperty({required: true})
    public password: string;
}
