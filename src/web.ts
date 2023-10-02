import { WebPlugin } from '@capacitor/core';

import type { CapacitorHeicToJpegPlugin } from './definitions';

export class CapacitorHeicToJpegWeb
  extends WebPlugin
  implements CapacitorHeicToJpegPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
