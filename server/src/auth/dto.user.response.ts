import { ApiResponseModelProperty, ApiModelProperty } from '@nestjs/swagger';

export class DtoUserResponse {
    @ApiResponseModelProperty()
    public email: string;
    @ApiModelProperty({ required: false })
    public firstName?: string;
    @ApiModelProperty({ required: false })
    public lastName?: string;
    @ApiModelProperty({ required: false })
    public phone?: string;
}
